package com.bhhan.example.customer.config;

import com.bhhan.example.common.config.CommonSwaggerConfiguration;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by hbh5274@gmail.com on 2020-11-23
 * Github : http://github.com/bhhan5274
 */

@Profile("!test")
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api(TypeResolver typeResolver){
        return CommonSwaggerConfiguration.api(typeResolver);
    }
}
