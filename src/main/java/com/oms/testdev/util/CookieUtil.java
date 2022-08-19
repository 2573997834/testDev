package com.oms.testdev.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: lym
 * @Description:
 * @Date: 2022/8/15 17:59
 */
public class CookieUtil {

    public static void addCookie(String cookieName, String cookieValue
            , int expireTime, HttpServletResponse response) {
        //通过cookie名字,cookie值创建对象
        Cookie cookie = new Cookie(cookieName, cookieValue);
        //跨域共享cookie的方法：设置cookie.setDomain(“”);
        cookie.setDomain("localhost");
        //设置过期时间
        cookie.setMaxAge(expireTime);
        //可在同一应用服务器内共享方法：设置cookie.setPath(“/”);
        cookie.setPath("/");
        //响应给浏览器
        response.addCookie(cookie);
    }
}
