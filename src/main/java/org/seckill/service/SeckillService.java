
package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * @author Linweizhe 按使用者的角度去设计
 */
public interface SeckillService {

    /**
     * 获取所有秒杀记录
     * 
     * @return
     */
    List<Seckill> getAllSeckill();

    /**
     * 获取单个秒杀记录
     * 
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 在秒杀开启时输出秒杀地址否则输出秒杀时间
     * 
     * @param seckillId
     */
    Exposer exposeSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作，允许抛出异常，因为可能成功，可能失败
     * 
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, SeckillCloseException, RepeatKillException;
}
