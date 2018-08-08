
package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * @author Linweizhe ��ʹ���ߵĽǶ�ȥ���
 */
public interface SeckillService {

    /**
     * ��ȡ������ɱ��¼
     * 
     * @return
     */
    List<Seckill> getAllSeckill();

    /**
     * ��ȡ������ɱ��¼
     * 
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * ����ɱ����ʱ�����ɱ��ַ���������ɱʱ��
     * 
     * @param seckillId
     */
    Exposer exposeSeckillUrl(long seckillId);

    /**
     * ִ����ɱ�����������׳��쳣����Ϊ���ܳɹ�������ʧ��
     * 
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, SeckillCloseException, RepeatKillException;
}
