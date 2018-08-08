package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;



/**
 * @author Linweizhe
 *��װִ����ɱ��Ľ��:�Ƿ���ɱ�ɹ�
 */
public class SeckillExecution {

    private long seckillId;

    private int state;
    
    private String stateInfo;
    //����ɱ�ɹ�ʱ����Ҫ������ɱ�ɹ��Ķ����ȥ
    private SuccessKilled successKilled;

    //��ɱ�ɹ�����������Ϣ
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();// ��ö�����е����ݷ��ڴ˷�װ����
        this.stateInfo = stateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    //��ɱʧ��
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }


    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
    


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    @Override
    public String toString() {
        return "SeckillExecution [seckillId=" + seckillId + ", state=" + state + ", stateInfo=" + stateInfo
                + ", successKilled=" + successKilled + "]";
    }

   
}