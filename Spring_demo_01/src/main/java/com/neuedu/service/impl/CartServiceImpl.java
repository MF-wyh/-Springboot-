package com.neuedu.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.common.CheckEnum;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServeResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.service.IProductService;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.List;


@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    IProductService productService;
    @Autowired
    CartMapper cartMapper;

    @Override
    public ServeResponse addProductToCart(Integer userId, Integer productId, Integer count) {


        //step1:参数的非空判断

        if (productId == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"商品ID必传");
        }
        if (count == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"商品数量不能为0");

        }

        //step2:判断商品是否存在

         ServeResponse<Product> serveResponse = productService.findProductById(productId);
         if (!serveResponse.isSuccess()){//商品不存在

             return ServeResponse.serveResponseByError(serveResponse.getStatus(),serveResponse.getMsg());
         }else{
             Product product = serveResponse.getData();
             if (product.getStock() < 0){
                 return ServeResponse.serveResponseByError(ResponseCode.ERROR,"商品已售空");
             }
         }



        //step3:商品是否在购物车中

        Cart cart = cartMapper.findCartByUseridAndProductId(userId,productId);
         if (cart == null){
             //添加操作
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setQuantity(count);
            newCart.setChecked(CheckEnum.CART_PRODUCT_CHECK.getCheck());
            int result = cartMapper.insert(newCart);
            if (result <= 0){
                return ServeResponse.serveResponseByError(ResponseCode.ERROR,"添加失败");
            }

         }else{
             //更新商品在购物车中的数量
             cart.setQuantity(cart.getQuantity()+count);
             int result = cartMapper.updateByPrimaryKey(cart);
             if (result <= 0){
                 return ServeResponse.serveResponseByError(ResponseCode.ERROR,"更新失败");
             }

         }


        //step4:封装购物车对象CartVO
        CartVO cartVO = getCartVO(userId);



        //step5:返回购物车CartVO
        return ServeResponse.serveResponseBySuccess(cartVO);


    }

    @Override
    public ServeResponse<List<Cart>> findCartsByUseridAndChecked(Integer userId) {

       List<Cart> cartList = cartMapper.findCartsByUseridAndChecked(userId);


        return ServeResponse.serveResponseBySuccess(cartList);
    }

    @Override
    public ServeResponse deleteBatch(List<Cart> cartList) {

        if (cartList == null||cartList.size() == 0){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"要删除的购物车商品不能为空");
        }

        int result = cartMapper.deleteBatch(cartList);
        if (result != cartList.size()){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"购物车清空失败");
        }

        return ServeResponse.serveResponseBySuccess();
    }

    private CartVO getCartVO(Integer userId){

        CartVO cartVO = new CartVO();

        //step1:根据userId查询该用户的购物信息->List<Cart>

        List<Cart> cartList = cartMapper.findCartsByUserid(userId);
        if (cartList == null||cartList.size() == 0){
            return cartVO;
        }

        //定义购物车商品总价格
        BigDecimal cartTotalPrize = new BigDecimal("0");
        //step2:List<Cart> --> List<CartProductVO>


        List<CartProductVO> cartProductVOList = Lists.newArrayList();
        int limit_Quantity = 0;
        String limitQuantity = null;
        for(Cart cart:cartList){
            //Cart --> CartProductVO
            CartProductVO cartProductVO = new CartProductVO();
            cartProductVO.setId(cart.getId());
            cartProductVO.setUserId(userId);
            cartProductVO.setProductId(cart.getProductId());

            ServeResponse<Product> serveResponse = productService.findProductById(cart.getProductId());

            if (serveResponse.isSuccess()){

                Product product = serveResponse.getData();
                if (product.getStock() >= cart.getQuantity()){
                    limit_Quantity = cart.getQuantity();
                    limitQuantity = "LIMIT_NUM_SUCCESS";
                }else{
                    limit_Quantity = product.getStock();
                    limitQuantity = "LIMIT_NUM_FAILED";
                }
                cartProductVO.setQuantity(limit_Quantity);
                cartProductVO.setLimitQuantity(limitQuantity);
                cartProductVO.setProductName(product.getName());
                cartProductVO.setProductSubtitle(product.getSubtitle());
                cartProductVO.setProductMainImage(product.getMainImage());
                cartProductVO.setProductPrice(product.getPrice());
                cartProductVO.setProductStatus(product.getStatus());
                //step3:计算购物车总价格
                cartProductVO.setProductTotalPrize(BigDecimalUtils.mul(product.getPrice().doubleValue(),
                        cart.getQuantity()*1.0));
                cartProductVO.setProductStock(product.getStock());
                cartProductVO.setProductChecked(cart.getChecked());
                cartProductVOList.add(cartProductVO);
                if (cart.getChecked() == CheckEnum.CART_PRODUCT_CHECK.getCheck()){

                    //商品被选中
                    cartTotalPrize = BigDecimalUtils.add(cartTotalPrize.doubleValue(),cartProductVO.getProductTotalPrize().doubleValue());

                }
            }
        }
        cartVO.setCartProductVOList(cartProductVOList);

        cartVO.setCarttotalprice(cartTotalPrize);

        //step4:判断是否全选
        Integer isAllChecked = cartMapper.isAllChecked(userId);

        if (isAllChecked == 0){
            //全选
            cartVO.setIsallchecked(true);
        }else{
            cartVO.setIsallchecked(false);
        }
        //step5:构建cartvo
        return cartVO;
    }


}
