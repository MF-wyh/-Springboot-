package com.neuedu.service;

import com.neuedu.common.ServeResponse;
import com.neuedu.pojo.Category;
import org.springframework.web.bind.annotation.RequestMapping;

public interface ICategoryService {
    /*
     * 添加类别
     * */
    @RequestMapping("/add_category.do")
    public ServeResponse addCategory(Category category);
    /*
     * 修改类别
     *categoryId
     * categoryName
     * categoryUrl
     */
    public ServeResponse updateCategory(Category category);
    /*
     * 查看平级类别
     * categoryId
     * */
    public ServeResponse getCategoryById(Integer categoryId);
    /*
     * 查看平级类别
     * categoryId
     * */

    public ServeResponse deepCategory( Integer categoryId);


    /*
    *根据id查询类别
    * */

    public ServeResponse<Category> selectCategory(Integer categoryId);
}
