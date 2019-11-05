package com.neuedu.common;

public enum PaymentEnum {


    PAYMENT_ONLINE(1,"在线支付"),
    PAYMENT_OFFLINE(2,"货到付款"),



    ;
    //0-已取消 10-未付款 20-已付款 40-已发货 50-交易成功 60-交易关闭
    private int Status;
    private String desc;
    PaymentEnum(int Status, String desc){
        this.Status = Status;
        this.desc = desc;
    }

    //枚举变量
    public static PaymentEnum codeOf(Integer status){
        for (PaymentEnum orderStatusEnum:values()){
            if(orderStatusEnum.getStatus() == status){
                return orderStatusEnum;
            }
        }
        return null;
    }


    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
