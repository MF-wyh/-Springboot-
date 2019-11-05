package com.neuedu.controller.backend;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServeResponse;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.Const;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage")
public class UserManageController {

    @Autowired
    IUserService userService;
    @RequestMapping(value = "login.do/{username}/{password}")
    public ServeResponse login(@PathVariable("username")String username,
                               @PathVariable("password")String password,
                               HttpSession session){
        ServeResponse serverResponse = userService.login(username,password, RoleEnum.ROLE_ADMIN.getRole());
        //判断是否成功登录
        if (serverResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }else{
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"密码错误");
        }

        return serverResponse;
    }


}
