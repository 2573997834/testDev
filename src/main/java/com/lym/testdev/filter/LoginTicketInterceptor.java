//package com.oms.testdev.filter;
//
//import com.oms.testdev.config.RedisCache;
//import com.oms.testdev.exception.NotLoginException;
//import com.oms.testdev.model.UserInfo;
//import com.oms.testdev.util.JwtUtil;
//import com.oms.testdev.util.SessionUser;
//import io.jsonwebtoken.Claims;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Objects;
//
//@Component
//public class LoginTicketInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private RedisCache redisCache;
//
//    // 保存登录信息
//    // 调用时间：Controller方法处理之前
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String requestURI = request.getRequestURI();
//        String token = !"null".equals(request.getHeader("Authorization")) ? request.getHeader("Authorization") : "";
//        if (StringUtils.isNotEmpty(token)) {
//            //解析token
//            String userId;
//            try {
//                Claims claims = JwtUtil.parseJWT(token);
//                userId = claims.getSubject();
//            } catch (Exception e) {
//                throw new NotLoginException("账号登录已超时，请重新登录");
//            }
//            //从redis中获取用户信息
//            String redisKey = "login:" + userId;
//            UserInfo loginUser = redisCache.getCacheObject(redisKey);
//            if (Objects.isNull(loginUser)) {
//                throw new NotLoginException("账号登录已超时，请重新登录");
//            }
//            SessionUser.addCurrentUser(loginUser);
//            return true;
//        }
//        return false;
//    }
//
//    // 调用时间：Controller方法处理完之后
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    // 调用时间：DispatcherServlet进行视图的渲染之后
//    // 请求结束，把保存的用户信息清除掉
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        SessionUser.remove();
//    }
//}
//
