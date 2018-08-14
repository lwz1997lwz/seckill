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
    // ��ȡ��־����
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private JedisDao jedisDao;
    // ����һ�������ַ���(��ɱ�ӿ�)��salt��Ϊ���ұ����û��³����ǵ�md5ֵ��ֵ�������Խ����Խ��
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
        //�ȴ�redis�в���
        Seckill seckill = jedisDao.getSeckill(seckillId);
        if (seckill == null) {
             seckill = getById(seckillId);
         // ˵����ѯ���������¼
            if (seckill == null) {
                return new Exposer(false, seckillId);
            }else {
                //д��redis����
                jedisDao.putSeckill(seckill);
            }
        }
        
        // ���ڹ涨ʱ������ɱ������
        Date nowTime = new Date();
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        // ת��Ϊ�ض��ַ��� ������
        String md5 = getMD5(seckillId);
        // ������ɱ��������ɱ��Ʒ��id �� �ø��ӿڼ��ܵ� md5ֵ
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    
    // ��ɱ�Ƿ�ɹ����ɹ�:����棬������ϸ��ʧ��:�׳��쳣������ع�
    @Override
    @Transactional(rollbackFor={SeckillException.class,RepeatKillException.class,})
    public SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, SeckillCloseException, RepeatKillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("��ɱ���ݱ��޸�");
        }

        // ִ����ɱ�߼�
        Date nowTime = new Date();
        //�׳�runtime�쳣����ʹ����ع���֤ԭ����
        try {
            //���빺����ϸ
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                throw new SeckillException("��ɱʧ��");
            } else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("�ظ���ɱ");
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
            // ���Ա������쳣ת��Ϊ�������쳣
            throw new SeckillException("seckill inner error :" + e.getMessage());
        }
    }

}
