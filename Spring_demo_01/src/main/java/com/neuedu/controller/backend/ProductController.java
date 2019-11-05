package com.neuedu.controller.backend;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServeResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.User;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utils.Const;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/product/")
public class ProductController {

    @Autowired
    IProductService productService;
    /*
    * 商品添加&更新
    * */
    @RequestMapping(value = "save.do")
    public ServeResponse addOrUpdate(Product product, HttpSession session){

        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){

            return ServeResponse.serveResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }
        int role = user.getRole();
        if (role == RoleEnum.ROLE_USER.getRole()){

            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"权限不足");
        }
        return productService.addOrUpdate(product);

    }
/*
* 商品上下架
* */


    /*
    * thymeleaf
    * */
/*
* 搜索商品
* */

    @RequestMapping(value = "search.do")
    public ServeResponse search(@RequestParam(name = "productName",required = false)String productName,
                                @RequestParam(name = "productId",required = false)Integer productId,
                                @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                                HttpSession session){


        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){


            return ServeResponse.serveResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }
        int role = user.getRole();
        if (role == RoleEnum.ROLE_USER.getRole()){

            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"权限不足");
        }

        return productService.search(productName, productId, pageNum, pageSize);
    }

    @RequestMapping(value = "/{productId}")
    public ServeResponse detail(@PathVariable("productId") Integer productId,HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServeResponse.serveResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }
        int role = user.getRole();
        if (role == RoleEnum.ROLE_USER.getRole()){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"权限不足");
        }
        return productService.detail(productId);
    }
}
