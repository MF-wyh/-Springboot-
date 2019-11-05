package com.neuedu.service.impl;
import com.google.common.collect.Sets;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServeResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public ServeResponse addCategory(Category category) {

        if (category == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"参数不能为空");
        }
        System.out.println(category.toString());
        int result = categoryMapper.insert(category);
        if (result < 0){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"添加品类失败");
        }
        return ServeResponse.serveResponseBySuccess();
    }

    @Override
    public ServeResponse updateCategory(Category category) {
        if (category == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"参数不能为空");
        }

        if (category.getId() == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"类别id必须传");
        }

        int result = categoryMapper.updateByPrimaryKey(category);
        if (result < 0){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"更新品类失败");
        }
        return ServeResponse.serveResponseBySuccess();
    }

    @Override
    public ServeResponse getCategoryById(Integer categoryId) {

        if (categoryId == null){
            return  ServeResponse.serveResponseByError(ResponseCode.ERROR,"id必传");
        }
        List<Category> categoryList = categoryMapper.selectCategoryById(categoryId);

        return ServeResponse.serveResponseBySuccess(categoryList,"成功");
    }

    @Override
    public ServeResponse deepCategory(Integer categoryId) {

        if (categoryId == null){
            return  ServeResponse.serveResponseByError(ResponseCode.ERROR,"id必传");
        }


        Set<Category> categorySet = Sets.newHashSet();
        //递归查询
        Set<Category> categorySet1 = findAllChildCategory(categorySet,categoryId);
        Set<Integer> categoryIds = Sets.newHashSet();
        Iterator<Category> iterator = categorySet1.iterator();
        while (iterator.hasNext()){
            Category category = iterator.next();
            categoryIds.add(category.getId());
        }

        return ServeResponse.serveResponseBySuccess(categoryIds);
    }

    @Override
    public ServeResponse<Category> selectCategory(Integer categoryId) {

        if (categoryId ==null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"类别必传");
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        return ServeResponse.serveResponseBySuccess(category);
    }

    public Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){
        //查看categoryID的类别信息
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            categorySet.add(category);
        }
        //查看category的平级子类
        List<Category> categoryList = categoryMapper.selectCategoryById(categoryId);
        if (categoryList != null && categoryList.size() > 0){
            for (Category category1 : categoryList) {//遍历集合
                findAllChildCategory(categorySet,category1.getId());
            }
        }
        return categorySet;
    }
}
