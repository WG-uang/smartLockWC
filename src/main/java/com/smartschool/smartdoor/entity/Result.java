package com.smartschool.smartdoor.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class Result implements Serializable {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private Integer status;
    private String msg;
    private Object data;

    public static ObjectMapper getMAPPER() {
        return MAPPER;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result(){}

    public Result(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Result(Object data) {
        this.status=1;
        this.msg="OK";
        this.data = data;
    }
    public static Result ok(){
        return new Result(null);
    }
    public static Result ok(Object data){
        return new Result(data);
    }
    public static Result build(Integer status,String msg){
        return new Result(status,msg,null);
    }
    public static Result build(Integer status,String msg,Object data){
        return new Result(status,msg,data);
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
