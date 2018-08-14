package org.seckill.dao;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lin_wz ????spring??junit?????junit?????????springIOC???? spring-test??junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-mybatis.xml")
@Transactional(transactionManager = "transactionManager",rollbackFor=Exception.class )
public class SeckillDaoTest {

    /**
     * ???seckillDao
     */
    @Resource
    private SeckillDao seckillDao;

    @Test
    @Rollback(true)
    public void testReduceNumber() {
        Date date = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, date);
        System.out.println("updateCount is " + updateCount);
    }

    @Test
    public void testQueryById() {
        long id = 1001;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void testQueryAll() {
        List<Seckill> seckill = seckillDao.queryAll(0, 100);
        for (Seckill seckill2 : seckill) {
            System.out.println(seckill2);
        }
    }

}
