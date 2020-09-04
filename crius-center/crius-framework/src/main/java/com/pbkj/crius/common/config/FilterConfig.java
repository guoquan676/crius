package com.pbkj.crius.common.config;

import com.pbkj.crius.common.filter.ServiceExceptionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 注册 过滤器使用
 * @author GZQ
 */
@Configuration
public class FilterConfig {

    /**
     * 用于对ServiceException的统一处理
     */
    @Bean
    public FilterRegistrationBean serviceExceptionFilter() {
        FilterRegistrationBean<ServiceExceptionFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ServiceExceptionFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }
    /**
     * 支持跨域请求
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
//    /**
//     * 用于用户token校验
//     *
//     * @return
//     */
//    @Bean
//    public FilterRegistrationBean userTokenFilter() {
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(new UserTokenFilter());
//        bean.addUrlPatterns("/*");
////        bean.addInitParameter("excludedUri", USER_TOKEN_EXCLUDE_PATH); 这样配置就是不生效。。。。哎 只能放在filter本身里了
//        bean.setOrder(2);
//        return bean;
//    }
}
