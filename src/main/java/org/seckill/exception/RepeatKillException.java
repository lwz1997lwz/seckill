package org.seckill.exception;

/**
 * 该异常为运行时异常 不需要我们手动捕获 
 * mysql 只支持 runtime异常的事务回滚
 * @author Linweizhe
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatKillException(String message) {
        super(message);
    }

}
