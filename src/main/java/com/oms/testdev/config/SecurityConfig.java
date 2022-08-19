package com.oms.testdev.config;

import com.oms.testdev.filter.ExceptionHandlerFilter;
import com.oms.testdev.filter.JwtAuthenticationTokenFilter;
import com.oms.testdev.security.MyAccessDecisionManager;
import com.oms.testdev.security.MyInvocationSecurityMetadataSourceService;
import com.oms.testdev.security.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author: lym
 * @Description:
 * @Date: 2022/8/11 9:40
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private CustomAuthenticationProvide customAuthenticationProvide;
    @Autowired
    private MyUserDetailService myUserDetailService;
//    @Autowired
//    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;
    @Autowired
    private ExceptionHandlerFilter exceptionHandlerFilter;
    @Autowired
    private MyAccessDecisionManager accessDecisionManager;
    @Autowired
    private MyInvocationSecurityMetadataSourceService metadataSource;

    /**
     * 强散列哈希加密实现
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 将认证入口AuthenticationManager注入容器中用于用户认证
     **/
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 身份认证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(customAuthenticationProvide);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //X-Frame-Options有以下配置项：
        //1.DENY：不能被嵌入到任何iframe或者frame中。
        //2.SAMEORIGIN：页面只能被本站页面嵌入到iframe或者frame中
        //3.ALLOW-FROM uri：只能被嵌入到指定域名的框架中。
        http.headers().frameOptions().sameOrigin();
        //授权
        http.authorizeRequests()
                //放行登录页面
                //.antMatchers("/").permitAll()
                .antMatchers("/doLogin").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/userNameCheck").permitAll()
                //放行静态资源
                .antMatchers("/**/*.html", "/**/*.css",
                "/**/*.js", "/**/*.ttf", "/**/*.woff", "/**/*.ico", "/**/*.png", "/**/*.jpg").permitAll()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(accessDecisionManager);
                        object.setSecurityMetadataSource(metadataSource);
                        return object;
                    }
                })
                //所有请求都必须被认证
                .anyRequest().authenticated();

        //注销
        http.logout()
                //退出登录url
                .logoutUrl("/logout")
                //退出登录成功跳转url
                .logoutSuccessUrl("/toLogin");

        // 参数列表分别为需要插入的过滤器和标识过滤器的字节码
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        http.addFilterBefore(exceptionHandlerFilter, CorsFilter.class);
        //不通过Session获取SecurityContext
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //异常处理（例如权限不足）
        //http.exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler("/to403"));  //重定向

        //记住我
//        http.rememberMe()
//                //自定义登录逻辑
//                .userDetailsService(userDetailsService)
//                //指定存储位置
//                .tokenRepository(tokenRepository);

        //将过滤器配置在UsernamePasswordAuthenticationFilter之前
        //http.addFilterBefore(new VerificationCodeFilter(failureHandler), UsernamePasswordAuthenticationFilter.class);

        //单点登录
//        http.sessionManagement()
//                .invalidSessionUrl("/login")
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(false);

        //关闭csrf防护
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

    }
}

