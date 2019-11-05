package com.neuedu.service;
import com.neuedu.common.ServeResponse;
import com.neuedu.pojo.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public interface IUserService {
    /*
    *@param user
    * @return ServeResponse
    * 注册接口
    * */
    public ServeResponse register(User user);
    /*
    * 登录接口
    * @Param username password
    *               type 1:普通用户
    *               type 0:管理员
    * @return ServeResponse
    * */
    public ServeResponse login(String username,String password,int type);



    /*
     * 根据username获取密保问题
     * */
    @RequestMapping("forget_get_question/{username}")
    public ServeResponse forget_get_question(@PathVariable("username") String username);


    /*
     * 提交答案
     * */
    @RequestMapping("forget_check_answer.do")
    public ServeResponse forget_check_answer(String username,String question,String answer);
    /*
     * 重置密码
     * */
    @RequestMapping("forget_reset_password.do")
    public ServeResponse forget_reset_password(String username,String newpassword,String forgettoken);
    /*
    * 更新个人信息
    * */
    public ServeResponse update_information(User user);
}
