package org.seckill.service.impl;

import java.util.Date;
import java.util.List;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.JedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * @author Linweizhe
 *
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    // 获取日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private JedisDao jedisDao;
    // 加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出我们的md5值，值任意给，越复杂越好
    private final String salt = "kjdlak**(&^&*Lklad";

    @Override
    public List<Seckill> getAllSeckill() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exposeSeckillUrl(long seckillId) {
        //先从redis中查找
        Seckill seckill = jedisDao.getSeckill(seckillId);
        if (seckill == null) {
             seckill = getById(seckillId);
         // 说明查询不到这个记录
            if (seckill == null) {
                return new Exposer(false, seckillId);
            }else {
                //写入redis缓存
                jedisDao.putSeckill(seckill);
            }
        }
        
        // 不在规定时间内秒杀不开启
        Date nowTime = new Date();
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        // 转化为特定字符串 不可逆
        String md5 = getMD5(seckillId);
        // 开启秒杀并返回秒杀商品的id 和 用给接口加密的 md5值
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    
    // 秒杀是否成功，成功:减库存，增加明细；失败:抛出异常，事务回滚
    @Override
    @Transactional(rollbackFor={SeckillException.class,RepeatKillException.class,})
    public SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, SeckillCloseException, RepeatKillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("秒杀数据被修改");
        }

        // 执行秒杀逻辑
        Date nowTime = new Date();
        //抛出runtime异常可以使事务回滚保证原子性
        try {
            //插入购买明细
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                throw new SeckillException("秒杀失败");
            } else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("重复秒杀");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所以编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error :" + e.getMessage());
        }
    }

}
