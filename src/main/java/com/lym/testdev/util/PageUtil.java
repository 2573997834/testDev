package com.lym.testdev.util;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: lym
 * @Description:
 * @Date: 2022/8/18 15:03
 */
public class PageUtil<T> extends PageInfo<T> {

    private int status = 0;

    private String msg;

    private T param;

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

    public PageUtil() {
    }

    public PageUtil(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public PageUtil(int status, String msg, List<T> list) {
        super(list);
        this.status = status;
        this.msg = msg;
    }

    public static <U> PageUtil<U> success(String msg, List<U> list) {
        return new PageUtil(0, msg, list);
    }

    public static PageUtil success(String msg) {
        return new PageUtil(0, msg);
    }

    public static PageUtil success() {
        return new PageUtil(0, "");
    }

    public static <U> PageUtil<U> fail(String msg) {
        return new PageUtil(1, msg);
    }

    public static <U> PageUtil<U> fail() {
        return new PageUtil(1, "");
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
