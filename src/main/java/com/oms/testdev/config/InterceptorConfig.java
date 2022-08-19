//package com.oms.testdev.config;
//
//import com.oms.testdev.filter.LoginTicketInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class InterceptorConfig implements WebMvcConfigurer {
//
//    @Bean
//    public LoginTicketInterceptor setLoginTicketInterceptor(){
//        return new LoginTicketInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(setLoginTicketInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/doLogin","/error","/**/*.html","/**/*.js","/**/*.css","/**/*.ttf","/**/*.woff","/**/*.jsp","/**/*.gif","/**/*.png");
//    }
//
//}
