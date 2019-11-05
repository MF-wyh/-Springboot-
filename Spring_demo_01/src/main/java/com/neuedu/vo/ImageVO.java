package com.neuedu.vo;
public class ImageVO {

    public String uri;
    public String url;//图片完整路径名称

    public ImageVO(){}
   public ImageVO(String uri,String url){
       this.uri = uri;
       this.url = url;
    }
}
