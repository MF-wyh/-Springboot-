package com.neuedu.service.impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServeResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServeResponse add(Shipping shipping) {

        //step1:参数的非空判断

        if (shipping == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"参数必传");

        }

        Integer shippingid = shipping.getId();
        if (shippingid == null){

            //添加
            int result = shippingMapper.insert(shipping);
            if (result <= 0){
                return ServeResponse.serveResponseByError(ResponseCode.ERROR,"添加地址失败");
            }else {
                return ServeResponse.serveResponseBySuccess(shipping.getId());
            }

        }else{
            //更新(未写完)

        }
        return null;
    }

    @Override
    public ServeResponse findShippingById(Integer shippingid) {
        if (shippingid == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"shippingid必传");
        }
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingid);
        if (shipping == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"收货地址不存在");
        }
        return ServeResponse.serveResponseBySuccess(shipping);
    }
}
