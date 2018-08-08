package org.seckill.dao;


import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lwz
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-mybatis.xml")
@Transactional(transactionManager = "transactionManager",rollbackFor=Exception.class )
public class SuccessKilledDaoTest {
    @Resource
    SuccessKilledDao successKilledDao;
    @Rollback(true)
    @Test
    public void testInsertSuccessKilled() {
        Long userPhone = 13254654465L;
        successKilledDao.insertSuccessKilled(1000L, userPhone);
    }

    @Test
    public void testQueryByIdWithSeckill() {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000L, 13254654465L);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}
