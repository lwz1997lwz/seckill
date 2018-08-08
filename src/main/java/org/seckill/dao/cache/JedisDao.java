package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisDao {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;
    Schema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
    public JedisDao(String host, int port) {

        this.jedisPool = new JedisPool(host, port);
    }

    public String putSeckill(Seckill seckill) {
        //定义缓冲
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        //定义一个模式
        
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                int timeout = 60 * 60;
               // byte[] value = JdkSerialize.serialize(seckill); 采用jdk自带序列化 效率低下
                //采用protobuf 序列化对象为字节数组
                byte[] value = ProtostuffIOUtil.toByteArray(seckill, schema, buffer);
                String result = jedis.setex(key.getBytes(), timeout, value);
                return result;
            } finally {
                jedis.close();
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public Seckill getSeckill(long seckillId) {
   

        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;

                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    //创建一个空对象 用于反序列化
                    Seckill seckill = Seckill.class.newInstance();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }

            } finally {
                jedis.close();
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
