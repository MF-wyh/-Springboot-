package com.neuedu.service;
import com.neuedu.common.ServeResponse;
import com.neuedu.pojo.Product;
import com.neuedu.vo.ProductDetailVO;

public interface IProductService {

    public ServeResponse addOrUpdate(Product product);
    /*
    * 后台商品搜索
    * */
    public ServeResponse search(String productName,
                               Integer productId,
                               Integer pageNum,
                               Integer pageSize);

    //商品详情

    public ServeResponse<ProductDetailVO> detail(Integer productId);

    //商品详情

    public ServeResponse<Product> findProductByProductId(Integer productId);

    //根据商品ID查询商品信息(库存)

    public ServeResponse<Product> findProductById(Integer productId);


    //扣库存
    public ServeResponse reduceStock(Integer productId,Integer stock);



}
