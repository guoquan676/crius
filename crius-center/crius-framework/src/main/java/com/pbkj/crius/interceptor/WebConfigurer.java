package com.pbkj.crius.interceptor;

import com.pbkj.crius.common.config.StringToEnumConverterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yuanyang
 * @version 1.0
 * @date 2020/12/17 11:13
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverterFactory());

    }
}
