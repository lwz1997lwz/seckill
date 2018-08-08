package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * @author lwz
 *
 */
public interface SuccessKilledDao {

    /**
     * ���빺����ϸ ���Թ����ظ���ɱ
     * 
     * @param seckilledId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * ������Ʒid���û��ֻ����� ��ѯ��ϸ
     * 
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
