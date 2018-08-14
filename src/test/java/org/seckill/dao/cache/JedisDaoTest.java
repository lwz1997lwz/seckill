package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-mybatis.xml")
public class JedisDaoTest {
    @Autowired
    private JedisDao jedisDao;
    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() {
        int id = 1000;
        Seckill seckill = jedisDao.getSeckill(id);
        if (seckill == null) {
            seckill = seckillDao.queryById(id);
            if (seckill != null) {
                String result = jedisDao.putSeckill(seckill);
                System.out.println(result);
                seckill = jedisDao.getSeckill(id);
                System.out.println(seckill);
            }
        }
    }

}
