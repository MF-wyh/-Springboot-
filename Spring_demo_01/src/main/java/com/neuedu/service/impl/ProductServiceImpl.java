package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServeResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utils.DateUtils;
import java.util.List;


@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    ICategoryService categoryService;
    @Autowired
    ProductMapper productMapper;

    @Value("${shopping.imageHost}")
    private String imageHost;

    @Override
    public ServeResponse addOrUpdate(Product product) {

        if (product == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"参数必传");
        }


        //subimages 1.png,2.png,3.png
        //step2:设置商品的主图 sub_images --> 1.jpg 2.jpg 3.jpg
        String subImages = product.getSubImages();
        if (subImages != null && subImages.equals("")){
            String[] subImageArr = subImages.split(",");
            if (subImageArr.length > 0){
                //设置商品的主图
                product.setMainImage(subImageArr[0]);
            }
        }


        Integer productId = product.getId();
        if (productId == null){
            //添加
            int result = productMapper.insert(product);
            if (result <= 0){
                return ServeResponse.serveResponseByError(ResponseCode.ERROR,"添加失败");
            }else{
                return ServeResponse.serveResponseBySuccess();
            }
        }else{
            //更新
            int result = productMapper.insert(product);
            if (result <= 0){
                return ServeResponse.serveResponseByError(ResponseCode.ERROR,"更新失败");
            }else{
                return ServeResponse.serveResponseBySuccess();
            }
        }
    }

    @Override
    public ServeResponse search(String productName, Integer productId, Integer pageNum, Integer pageSize) {


        if (productName!= null){//按照商品名称进行模糊查询
            productName = "%" + productName + "%";
        }

        //
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.findProductsByNameAndId(productId,productName);

        List<ProductListVO> productListVOList = Lists.newArrayList();
        //List<Product> -> List<ProductListVO>
        if (productList != null && productList.size() > 0){
            for (Product product : productList) {
                //product -> productListVO
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);

            }
        }
        PageInfo pageInfo = new PageInfo(productList);

        return ServeResponse.serveResponseBySuccess(pageInfo);
    }

    //查看商品详情

    @Override
    public ServeResponse<ProductDetailVO> detail(Integer productId) {


        if (productId == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"参数必传");
        }

        Product product = productMapper.selectByPrimaryKey(productId);

        if (product == null){
            return ServeResponse.serveResponseBySuccess();
        }

        //product -> productVO
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);


        return ServeResponse.serveResponseBySuccess(productDetailVO);
    }

    @Override
    public ServeResponse<Product> findProductByProductId(Integer productId) {
        if (productId == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"参数必传");
        }

        Product product = productMapper.selectByPrimaryKey(productId);

        if (product == null){
            return ServeResponse.serveResponseBySuccess();
        }

        return ServeResponse.serveResponseBySuccess(product);
    }

    @Override
    public ServeResponse<Product> findProductById(Integer productId) {

        if (productId == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"商品id必传");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            //商品不存在
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"商品不存在");

        }

        return ServeResponse.serveResponseBySuccess(product);
    }

    @Override
    public ServeResponse reduceStock(Integer productId, Integer stock) {

        if (productId == null){

            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"商品id必传");
        }
        if (stock == null){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"库存参数必传");
        }
        int result = productMapper.reduceProductStock(productId,stock);
        if (result <= 0){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"扣库存失败");
        }

        return ServeResponse.serveResponseBySuccess();
    }


    private ProductDetailVO assembleProductDetailVO(Product product){


        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(imageHost);
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        // Category category= categoryMapper.selectByPrimaryKey(product.getCategoryId());
        ServeResponse<Category> serveResponse = categoryService.selectCategory(product.getCategoryId());
        Category category = serveResponse.getData();
        if (category == null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }
        return productDetailVO;
    }


    private ProductListVO assembleProductListVO(Product product){

        ProductListVO productListVO = new ProductListVO();

        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrize(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());

        return productListVO;
    }

}
