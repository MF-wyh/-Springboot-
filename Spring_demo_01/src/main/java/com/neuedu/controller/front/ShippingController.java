package com.neuedu.controller.front;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServeResponse;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.User;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.Const;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    IShippingService shippingService;

    @RequestMapping(value = "add.do")
    public ServeResponse add(Shipping shipping, HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null){
            return ServeResponse.serveResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }
        shipping.setUserId(user.getId());

        return shippingService.add(shipping);
    }


}
