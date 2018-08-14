package org.seckill.serviceimpl;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-mybatis.xml", "classpath:spring-service.xml" })
public class SeckillServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetAllSeckill() {
        List<Seckill> seckills = seckillService.getAllSeckill();
        logger.info("list:{}", seckills);
    }

    @Test
    public void testGetById() {
        Seckill seckill = seckillService.getById(1000);
        logger.info("seckill:{}", seckill);
    }

    @Test
    public void testExposeSeckillUrl() {
        long seckillId = 1003;
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
        logger.info("exposer:{}", exposer);
    }
    

    @Test
    public void testExcuteSeckill() {
        long seckillId = 1001;
        String md5 = "5d9523946372dad64aa26e1d7737fc2c";
        long userPhone = 13578929999L;

        testExcute(seckillId, md5, userPhone);
    }

    private void testExcute(long seckillId, String md5, long userPhone) {
        try {
            SeckillExecution seckillExecution = seckillService.excuteSeckill(seckillId, userPhone, md5);
            logger.info("seckillExecution:{}", seckillExecution);
        } catch (SeckillCloseException e1) {
            e1.printStackTrace();
        } catch (RepeatKillException e2) {
            e2.printStackTrace();
        }
    }

    /**
     * ���ڵ��ĸ����Է�����md5������������������ �����ʵ�ʲ����лὫ������������ϳ�һ���������߼����̽��в���
     */
    @Test
    public void testLogic() {
        long seckillId = 1001;
        long userPhone = 13578929999L;
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
        String md5 = exposer.getMd5();
        if (md5 != null) {
            testExcute(seckillId, md5, userPhone);
        } else {
            // ��ɱδ����
            logger.info("exposer:{}", exposer);
        }
    }
}
