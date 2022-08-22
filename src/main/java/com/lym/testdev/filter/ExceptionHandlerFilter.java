package com.lym.testdev.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lym.testdev.exception.NotLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 异常处理
 */
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (NotLoginException e) {
            String requestURI = request.getRequestURI();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            JSONObject object = new JSONObject();
            object.put("status", HttpStatus.UNAUTHORIZED.value());
            object.put("message", e.getMessage());
            object.put("path", requestURI);
            object.put("timestamp", new Date());
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(object));
        }
    }

}