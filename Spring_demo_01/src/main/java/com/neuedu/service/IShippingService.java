package com.neuedu.service;

import com.neuedu.common.ServeResponse;
import com.neuedu.pojo.Shipping;

public interface IShippingService {

    public ServeResponse add(Shipping shipping);
    public ServeResponse findShippingById(Integer shippingid);
}
