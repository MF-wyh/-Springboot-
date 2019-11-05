package com.neuedu.service;

import com.neuedu.common.ServeResponse;

import java.util.Map;

public interface IOrderService  {
    /*
    * 创建订单
    * */
    public ServeResponse createOrder(Integer userId,Integer shippingId);


    /*
    * 支付
    * */
    public ServeResponse pay(Integer userId,Long orderNo);

    /*
    * 支付宝回调接口
    * */
    public String callback(Map<String,String> requestParams);

}
