package com.oms.testdev.service;

import com.oms.testdev.config.RedisCache;
import com.oms.testdev.dao.UserInfoDao;
import com.oms.testdev.model.UserDetail;
import com.oms.testdev.model.UserInfo;
import com.oms.testdev.util.JwtUtil;
import com.oms.testdev.util.RSAUtils;
import com.oms.testdev.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lym
 * @Description:
 * @Date: 2022/8/12 9:37
 */
@Slf4j
@Service
public class UserServiceImpl {

    /**
     * 获取认证入口
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserInfoDao userInfoDao;

    public Result<Map<String, String>> login(UserInfo user) {
        Result<Map<String, String>> res = new Result<>(1, "用户名或密码错误", null);
        //3wTvMRs6ozyYjFZQu815FWocbHxxitE+BoXbXjpLdE1gTQyPYHDIHGmZGRGWbq0ItkKrFy1yhZT92TrzSAI4OA==
        Authentication authenticate = null;
        try {
            // 在没认证之前principal, credentials两个参数分别存放用户名和密码
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            // 通过AuthenticationManager的authenticate方法来进行用户认证
            authenticate = authenticationManager.authenticate(authenticationToken);
            // 判断是否验证成功
            if (Objects.isNull(authenticate)) {
                return res;
            }
        } catch (Exception e) {
            log.error("用户名或密码错误");
            return res;
        }
        try {
            if (null != authenticate) {
                // 在认证信息authenticate中获取登录成功后的用户信息
                UserDetail loginUser = (UserDetail) authenticate.getPrincipal();
                UserInfo userInfo = loginUser.getUser();
                // 使用userid生成token
                String userId = userInfo.getUserid();
                String jwt = JwtUtil.createJWT(userId);
                // userId用作key，将用户信息存入redis，并设置30分钟过期
                redisCache.setCacheObject("login:" + userId, userInfo, 2, TimeUnit.MINUTES);
                //权限菜单放入redis
                if (CollectionUtils.isNotEmpty(loginUser.getPaths())) {
                    redisCache.setCacheList("path:" + userId, loginUser.getPaths());
                }
                // 把token响应给前端
                HashMap<String, String> map = new HashMap<>();
                map.put("token", jwt);
                userInfo.setLoginTime(new Date());
                userInfoDao.updateByPrimaryKey(userInfo);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginUser, loginUser.getPassword(), loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                res = new Result<>(0, "登录成功", map);
            } else {
                res = new Result<>(1, "用户名或密码错误", null);
            }
        } catch (Exception e) {
            log.error("登录异常", e);
            res = new Result<>(1, "用户名或密码错误", null);
        }
        return res;
    }

    public Result register(UserInfo user) {
        user.setUserid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(RSAUtils.decrypt(user.getPassword())));
        user.setCreateTime(new Date());
        int i = userInfoDao.insertSelective(user);
        return new Result(0, "添加成功", null);
    }

    public Result userNameCheck(String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        int count = userInfoDao.selectCount(userInfo);
        if (count > 0) {
            return new Result(1, "用户名已存在！", null);
        } else {
            return new Result(0, "success", null);
        }
    }
}
