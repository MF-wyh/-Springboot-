package com.neuedu.controller.front;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServeResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.Const;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    IOrderService orderService;
    //step1:创建订单接口

    @RequestMapping("{shippingid}")
    public ServeResponse createOrder(@PathVariable("shippingid") Integer shippingid,
                                     HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServeResponse.serveResponseByError(ResponseCode.NOT_LOGIN,"未登录");

        }
        return orderService.createOrder(user.getId(),shippingid);
    }

    /*
    * 支付接口
    * */
    @RequestMapping("pay/{orderNo}")
    public ServeResponse pay(@PathVariable("orderNo")Long orderNo,HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"未登录");

        }

        return orderService.pay(user.getId(),orderNo);
    }

    /*
    * 支付宝服务器回调商家服务器接口
    * */
    @RequestMapping("callback.do")
    public String alipay_callback(HttpServletRequest request){

        Map<String,String[]> callbackParams = request.getParameterMap();//1,2,3,4
        Map<String,String> signParams = Maps.newHashMap();
        Iterator<String> iterator = callbackParams.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String[] values = callbackParams.get(key);
            StringBuffer sbuffer = new StringBuffer();
            if (values != null && values.length > 0){
                for (int i = 0;i < values.length;i++ ){
                    sbuffer.append(values[i]);
                    if(i != values.length - 1){
                        sbuffer.append(",");
                    }
                }
            }
            signParams.put(key,sbuffer.toString());
        }
        System.out.println(signParams);
        //验证签名
        try {
            signParams.remove("sign_type");
            boolean result = AlipaySignature.rsaCheckV2(signParams, Configs.getAlipayPublicKey(),"utf-8",
                    Configs.getSignType());
            if (result){
                //验证签名通过
                System.out.println("通过");
                return orderService.callback(signParams);
            }else{
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return "success";
    }
}
