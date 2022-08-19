package com.oms.testdev.security;

import com.oms.testdev.config.MenuLoad;
import com.oms.testdev.config.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.*;

@Slf4j
@Service
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    //此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        //若是静态资源 不做拦截  下面写了单独判断静态资源方法
        if (isMatcherAllowedRequest(filterInvocation)) {
            return null;
        }
        FilterInvocation fi = (FilterInvocation) object;
        //获取用户请求的Url
        String url = fi.getRequestUrl();
        List<String> deniedList = new ArrayList<>();
        for (String path : MenuLoad.menuList) {
            if (StringUtils.isNotEmpty(path)) {
                //逐一判断用户请求的Url是否和数据库中受权限控制的Url有匹配的
                if (antPathMatcher.match(path, url)) {
                    deniedList.add(path);
                }
            }
        }
        List<ConfigAttribute> list = SecurityConfig.createList(deniedList.toArray(new String[]{}));
        if (list.size() > 0) {
            return list;
        }
        return null;
    }


    /**
     * 判断当前请求是否在允许请求的范围内
     *
     * @param fi 当前请求
     * @return 是否在范围中
     */
    private boolean isMatcherAllowedRequest(FilterInvocation fi) {
        return allowedRequest().stream().map(AntPathRequestMatcher::new)
                .filter(requestMatcher -> requestMatcher.matches(fi.getHttpRequest()))
                .toArray().length > 0;
    }

    /**
     * @return 定义允许请求的列表
     */
    private List<String> allowedRequest() {
        return Arrays.asList("/page/index.html", "/page/register.html", "/page/login.html", "/page/welcome.html", "/register", "/doLogin", "/userNameCheck", "/**/*.js", "/**/*.css", "/**/*.ttf", "/**/*.woff", "/**/*.jpg");
    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}


