package com.oms.testdev.config;

import com.oms.testdev.security.MyUserDetailService;
import com.oms.testdev.util.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义进行逻辑验证
 */
@Component
public class CustomAuthenticationProvide implements AuthenticationProvider {

    //用户基本信息:用户表，角色表，用户-角色表
    @Autowired
    private MyUserDetailService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取用户的用户名和密码
        String username=authentication.getName();
        String password=authentication.getCredentials().toString();
        //获取数据库中能查询到的用户信息
        UserDetails userDetails=userDetailsService.loadUserByUsername(username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 如果是自定义AuthenticationProvider，需要手动密码校验
        if (!passwordEncoder.matches(RSAUtils.decrypt(password),userDetails.getPassword())){
            throw new BadCredentialsException("密码错误");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}


