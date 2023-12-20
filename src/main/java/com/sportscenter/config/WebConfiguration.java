package com.sportscenter.config;

import com.sportscenter.web.interceptor.LoggingInterceptor;
import com.sportscenter.web.interceptor.MaintenanceInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final MaintenanceInterceptor maintenanceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(loggingInterceptor);
        registry.addInterceptor(maintenanceInterceptor);
    }


}
