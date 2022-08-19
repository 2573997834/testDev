package com.oms.testdev.util;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 2120267584344923858L;

    private Integer status = 0;

    private String message = null;

    private T data = null;

    public Result() {

    }

    public Result(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <U> Result success() {
        return new Result(0, "", null);
    }

    public static <U> Result success(String message) {
        return new Result(0, message, null);
    }

    public static <U> Result success(String message, U data) {
        return new Result(0, message, data);
    }

    public static <U> Result fail() {
        return new Result(1, "", null);
    }

    public static <U> Result fail(String message) {
        return new Result(1, message, null);
    }

    public static <U> Result fail(String message, U data) {
        return new Result(1, message, data);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "com.lym.springboot.util.Result{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
