package com.neuedu.service.impl;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServeResponse;
import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.MD5Utils;
import utils.TokenCache;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServeResponse register(User user) {

        //step1:参数校验
        if(user == null){
            return ServeResponse.serveResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        //step2:判断用户名是否存在
        int result = userMapper.isexistsusername(user.getUsername());
        if(result > 0){
            //用户名已存在
            return ServeResponse.serveResponseByError(ResponseCode.USERNAME_EXISTS,"用户名已存在");
        }
        //step3：判断邮箱是否存在
        int resultemail = userMapper.isexistsemail(user.getEmail());
        if (resultemail > 0){
            //邮箱已存在
            return ServeResponse.serveResponseByError(ResponseCode.EMAIL_EXISTS,"邮箱已存在");
        }
        //step4:MD5密码加密，设置用户角色
        user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        //设置权限为普通用户
        user.setRole(RoleEnum.ROLE_USER.getRole());
        //step5:注册
        int insertResult = userMapper.insert(user);
        if(insertResult == 0){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"注册失败");
        }
        //step6:返回
        return ServeResponse.serveResponseBySuccess();
    }

    @Override
    public ServeResponse login(String username, String password,int type) {

        //step1:参数的非空校验

        //用户名是否为空
        if (username == null||username.equals("")){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"用户名为空");
        }
        //密码是否为空
        if (password == null||password.equals("")){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"密码为空");
        }
        //step2:判断用户名是否存在
        int result = userMapper.isexistsusername(username);
        if (result < 0){
            //用户名不存在
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"用户名不存在");
        }
        //step3:密码加密
        password = MD5Utils.getMD5Code(password);
        //step4：登录
       User user =  userMapper.findUserByUsernameAndPassword(username, password);
       if (user == null){//密码错误
           return ServeResponse.serveResponseByError(ResponseCode.ERROR,"密码错误");
       }
       if (type == 0){//管理员
            if (user.getRole() == RoleEnum.ROLE_USER.getRole()){//没有管理员权限
                return ServeResponse.serveResponseByError(ResponseCode.ERROR,"登录权限不足");
            }
       }
        return ServeResponse.serveResponseBySuccess(user);
    }

    @Override
    public ServeResponse forget_get_question(String username) {

        //step1:参数的非空校验
        if (username == null||username.equals("")){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"用户名不为空");
        }
        //step2:根据用户名查询问题
        String question = userMapper.forget_get_question(username);
        //step3:返回结果
        if (question == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"没有查询到密保问题");
        }
        return ServeResponse.serveResponseBySuccess(question);

    }

    @Override
    public ServeResponse forget_check_answer(String username, String question, String answer) {

        //step1:参数的非空校验

        if (username == null||username.equals("")){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"用户名不为空");
        }
        if (question == null||question.equals("")){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"密保问题不为空");
        }
        if (answer == null||answer.equals("")){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"答案不为空");
        }
        //step2:校验答案
        int result = userMapper.forget_check_answer(username,question,answer);
        //step3:返回结果
        if (result <= 0){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"答案错误");
        }
        //step3:返回结果
        //生成token(唯一标识)
        String token = UUID.randomUUID().toString();
        TokenCache.set("username:"+username, UUID.randomUUID().toString());
        return ServeResponse.serveResponseBySuccess(token);

    }

    @Override
    public ServeResponse forget_reset_password(String username, String newpassword, String forgettoken) {

        //step1:参数的非空校验

        if (username == null||username.equals("")){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"用户名不为空");
        }
        if (newpassword == null||newpassword.equals("")){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"新密码不为空");
        }
        if (forgettoken == null||forgettoken.equals("")){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"token不为空");
        }

        //是否修改的是自己的账号
        String token = TokenCache.get("username:"+username);
        if (token == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"不能修改别人的密码或者token已经过期");
        }
        if (!token.equals(forgettoken)){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"无效的token");
        }

        int result = userMapper.forget_reset_password(username,MD5Utils.getMD5Code(newpassword));
        if (result <= 0){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"密码修改失败");
        }
        return ServeResponse.serveResponseBySuccess();
    }

    @Override
    public ServeResponse update_information(User user) {

        //step1:非空校验
        if(user == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"参数不能为空");
        }
        int result = userMapper.updateUserByActive(user);
        if (result <= 0){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"修改失败");
        }
        return ServeResponse.serveResponseBySuccess();
    }
}
