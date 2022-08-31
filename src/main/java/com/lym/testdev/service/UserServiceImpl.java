package com.lym.testdev.service;

import com.lym.testdev.common.config.RedisCache;
import com.lym.testdev.common.exception.NoPermissionException;
import com.lym.testdev.common.model.MenuInfo;
import com.lym.testdev.dao.MenuInfoMapper;
import com.lym.testdev.dao.UserInfoDao;
import com.lym.testdev.common.model.UserDetail;
import com.lym.testdev.common.model.UserInfo;
import com.lym.testdev.common.util.JwtUtil;
import com.lym.testdev.common.util.RSAUtils;
import com.lym.testdev.common.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: lym
 * @Description:
 * @Date: 2022/8/12 9:37
 */
@Slf4j
@Service
public class UserServiceImpl {

    @Value("${login.timeout}")
    private Integer timeout;

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
    @Autowired
    private MenuInfoMapper menuInfoMapper;

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
        if (null != authenticate) {
            // 在认证信息authenticate中获取登录成功后的用户信息
            UserDetail loginUser = (UserDetail) authenticate.getPrincipal();
            UserInfo userInfo = loginUser.getUser();
            // 使用userid生成token
            String userId = userInfo.getUserid();
            String jwt = JwtUtil.createJWT(userId);
            // userId用作key，将用户信息存入redis，并设置30分钟过期
            redisCache.setCacheObject("login:" + userId, userInfo, timeout, TimeUnit.MINUTES);
            //权限菜单放入redis
            if (CollectionUtils.isNotEmpty(loginUser.getPaths())) {
                //redisCache.setCacheList("path:" + userId, loginUser.getPaths());
                redisCache.setCacheObject("path:" + userId, loginUser.getPaths());
            } else {
                throw new NoPermissionException("该账号无权限");
            }
            //将需要控制权限的所以菜单放入redis
            List<MenuInfo> menuInfoList = menuInfoMapper.selectAll();
            if (CollectionUtils.isNotEmpty(menuInfoList)) {
                List<String> list = menuInfoList.stream().filter(obj -> StringUtils.isNotEmpty(obj.getPath())).map(obj -> obj.getPath()).distinct().collect(Collectors.toList());
                redisCache.setCacheList("menus", list);
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
