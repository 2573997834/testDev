package com.lym.testdev.filter;

import com.lym.testdev.exception.NotLoginException;
import com.lym.testdev.model.UserInfo;
import com.lym.testdev.util.JwtUtil;
import com.lym.testdev.util.SessionUser;
import com.lym.testdev.config.RedisCache;
import com.lym.testdev.model.UserDetail;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String requestURI = request.getRequestURI();
        String type = "";
        if (requestURI.contains(".")) {
            type = requestURI.substring(requestURI.lastIndexOf("."));
            if (".js".equals(type) || ".css".equals(type) || ".jpg".equals(type)
                    || ".woff".equals(type) || ".ttf".equals(type) || ".ico".equals(type)) {
                filterChain.doFilter(request, response);
                return;
            }
        } else if ("/doLogin".equals(requestURI) || "/register".equals(requestURI) || "/userNameCheck".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        if ("/page/login.html".equals(requestURI) || "/page/register.html".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = "";
        Cookie[] cookieArr = request.getCookies();
        if (cookieArr != null && cookieArr.length > 0) {
            for (Cookie cookie : cookieArr) {
                if ("access_token".equals(cookie.getName())) {
                    if (StringUtils.isNotEmpty(cookie.getValue())) {
                        token = cookie.getValue();
                    }
                    break;
                }
            }
        }

        if (!"null".equals(request.getHeader("Authorization")) && StringUtils.isNotEmpty(request.getHeader("Authorization"))) {
            token = request.getHeader("Authorization");
        }

        if (StringUtils.isEmpty(token)) {
            if ("/".equals(requestURI) || ".html".equals(type)) {
                response.sendRedirect("/page/login.html");
            }
            throw new NotLoginException("账号登录已超时，请重新登录");
        } else {
            //解析token
            String userId;
            UserInfo loginUser = null;
            List<String> paths = null;
            try {
                Claims claims = JwtUtil.parseJWT(token);
                userId = claims.getSubject();
                //从redis中获取用户信息
                String redisKey = "login:" + userId;
                String redisKey2 = "path:" + userId;
                loginUser = redisCache.getCacheObject(redisKey);
                paths = redisCache.getCacheList(redisKey2);
                if (Objects.isNull(loginUser)) {
                    throw new NotLoginException("账号登录已超时，请重新登录");
                }
                //String userJson = JSON.toJSONString(userObj);
                //UserDetail loginUser = JSONObject.parseObject(userJson, UserDetail.class);

                //刷新token 重新计时token过期时间
                token = JwtUtil.refreshToken(token);
                // userId用作key，将用户信息存入redis，并设置30分钟过期
                redisCache.setCacheObject("login:" + userId, loginUser, 2, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error("系统异常", e);
                if ("/".equals(requestURI)) {
                    response.sendRedirect("/page/index.html");
                } else {
                    throw new NotLoginException("账号登录已超时，请重新登录");
                }
            }
            UserDetail userDetail = new UserDetail();
            userDetail.setUser(loginUser);
            userDetail.setPaths(paths);

            //存入SecurityContextHolder
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetail, loginUser.getPassword(), userDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            try {
                SessionUser.addCurrentUser(loginUser);
                response.setHeader("refresh_token", token);
                if ("/".equals(requestURI)) {
                    response.sendRedirect("/page/index.html");
                } else {
                    //放行
                    filterChain.doFilter(request, response);
                }
            } finally {
                SessionUser.remove();
            }
        }
    }
}

