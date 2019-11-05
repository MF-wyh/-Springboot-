package com.neuedu.controller.front;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServeResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.Const;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    ICartService cartService;
    /*
    * 添加商品到购物车
    * */

    @RequestMapping("add/{productId}/{count}")
    public ServeResponse addCart(@PathVariable("productId") Integer productId,
                                 @PathVariable("count") Integer count,
                                 HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null){
            return ServeResponse.serveResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }


        return cartService.addProductToCart(user.getId(),productId,count);
    }


}
