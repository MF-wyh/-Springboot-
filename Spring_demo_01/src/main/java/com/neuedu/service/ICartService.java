package com.neuedu.service;

import com.neuedu.common.ServeResponse;
import com.neuedu.pojo.Cart;

import java.util.List;

public interface ICartService {

    /*
    * 添加商品的购物车
    * */

    public ServeResponse addProductToCart(Integer userId,Integer productId,Integer count);


    /*
     * 根据用户ID来查看用户已选中的商品
     * */

    public ServeResponse<List<Cart>> findCartsByUseridAndChecked(Integer userId);


    /*
    * 批量删除购物车信息商品
    * */

    public ServeResponse deleteBatch(List<Cart> cartList);


}
