package com.lym.testdev.util;

import com.lym.testdev.model.UserInfo;

public class SessionUser {

    /**
     * 保存用户对象的ThreadLocal  在拦截器操作 添加、删除相关用户数据
     */
    private static final ThreadLocal<UserInfo> userThreadLocal = new ThreadLocal<UserInfo>();

    public static void addCurrentUser(UserInfo user){
        userThreadLocal.set(user);
    }

    public static UserInfo getCurrentUser(){
        return userThreadLocal.get();
    }

    public static void remove(){
        userThreadLocal.remove();
    }

}
