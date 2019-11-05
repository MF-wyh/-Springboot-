package com.neuedu.common;

public enum OrderStatusEnum {


    ORDER_CANCEL(0,"已取消"),
    ORDER_NO_PAY(10,"未付款"),
    ORDER_PAYED(20,"已付款"),
    ORDER_SENT(40,"已发货"),
    ORDER_SUCCESS(50,"交易成功"),
    ORDER_CLOSED(60,"交易关闭"),


    ;
    //0-已取消 10-未付款 20-已付款 40-已发货 50-交易成功 60-交易关闭
    private int Status;
    private String desc;
    OrderStatusEnum(int Status, String desc){
        this.Status = Status;
        this.desc = desc;
    }

    //枚举变量
    public static OrderStatusEnum codeOf(Integer status){
        for (OrderStatusEnum orderStatusEnum:values()){
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
