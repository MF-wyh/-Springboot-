package com.neuedu.common;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServeResponse<T> {

    //返回到前端的状态码
    private int status;
    //返回前端的数据
    private T data;
    //当status != 0时，封装了错误信息
    private String msg;

    private ServeResponse(){}
    private ServeResponse(int status){
        this.status = status;
    }
    private ServeResponse(int status,T data){
        this.status = status;
        this.data = data;
    }
    private ServeResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }
    private ServeResponse(int status,T data,String msg){
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    /*
    * 调用接口成功回调
    * */

    public static ServeResponse serveResponseBySuccess(){
        return new ServeResponse(ResponseCode.SUCCESS);
    }
    public static <T>ServeResponse serveResponseBySuccess(T data){
        return new ServeResponse(ResponseCode.SUCCESS,data);
    }
    public static <T>ServeResponse serveResponseBySuccess(T data,String msg){
        return new ServeResponse(ResponseCode.SUCCESS,data,msg);
    }
    /*
    * 接口调用失败时回调
    * */
    public static ServeResponse serveResponseByError(){
        return new ServeResponse(ResponseCode.ERROR);
    }
    public static ServeResponse serveResponseByError(String msg){
        return new ServeResponse(ResponseCode.ERROR,msg);
    }
    public static ServeResponse serveResponseByError(int status){
        return new ServeResponse(status);
    }
    public static ServeResponse serveResponseByError(int status,String msg){
        return new ServeResponse(status,msg);
    }

    /*
    * 判断接口是否正确返回
    * status = 0;
    * */

    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
