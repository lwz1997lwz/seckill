package org.seckill.exception;

/**
 * ���쳣Ϊ����ʱ�쳣 ����Ҫ�����ֶ����� 
 * mysql ֻ֧�� runtime�쳣������ع�
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
