package com.neuedu.dao;

import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbg.generated
     */
    int insert(Cart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbg.generated
     */
    Cart selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbg.generated
     */
    List<Cart> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Cart record);

    /*
    *根据id获取平级子类
    **/
    List<Category> selectCategoryById(int categoryId);


    /*
    * 根据用户id和商品id查询购物车信息
    * */

    Cart findCartByUseridAndProductId(@Param("userid")Integer userid,
                                      @Param("productid")Integer productId);


    /*
    * 查询用户的购物车信息
    * */
    List<Cart> findCartsByUserid(@Param("userid") Integer userid);

    /*
    *统计用户购物车中未选中的商品的数量
    */
    Integer isAllChecked(@Param("userid") Integer userid);


    /*
    * 查看购物车中用户勾选的商品
    * */

    List<Cart> findCartsByUseridAndChecked(@Param("userId") Integer userId);

    /*
    * 清空已选中的购物车
    * */

    int deleteBatch(@Param("cartList") List<Cart> cartList);



}