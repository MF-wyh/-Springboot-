package com.neuedu.controller.backend;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServeResponse;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.User;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.Const;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category/")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;
    /*
    * 添加类别
    * */
    @RequestMapping("/add_category.do")
    public ServeResponse addCategory(Category category, HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServeResponse.serveResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }

        int role = user.getRole();
        if (role == RoleEnum.ROLE_USER.getRole()){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"权限不足");
        }

        return categoryService.addCategory(category);

    }
    /*
    * 修改类别
    *categoryId
    * categoryName
    * categoryUrl
     */
    @RequestMapping("/set_category.do")
    public ServeResponse updateCategory(Category category,HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServeResponse.serveResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }

        int role = user.getRole();
        if (role == RoleEnum.ROLE_USER.getRole()){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"权限不足");
        }

        return categoryService.updateCategory(category);
    }
    /*
    * 查看平级类别
    * categoryId
    * */
    @RequestMapping("{/categoryId}")
    public ServeResponse getCategoryById(@PathVariable("categoryId") Integer categoryId,HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServeResponse.serveResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }

        int role = user.getRole();
        if (role == RoleEnum.ROLE_USER.getRole()){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"权限不足");
        }

        return categoryService.getCategoryById(categoryId);

    }
    /*
     * 递归查看类别
     * categoryId
     * */
    @RequestMapping("{/deep/categoryId}")
    public ServeResponse deepCategory(@PathVariable("categoryId") Integer categoryId,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServeResponse.serveResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }

        int role = user.getRole();
        if (role == RoleEnum.ROLE_USER.getRole()){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"权限不足");
        }

        return categoryService.deepCategory(categoryId);
    }







}
