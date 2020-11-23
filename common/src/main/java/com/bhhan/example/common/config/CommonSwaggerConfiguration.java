package com.bhhan.example.common.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */

public class CommonSwaggerConfiguration {
    private static final String DEFAULT_SWAGGER_PACKAGE = "com.bhhan.example";

    public static Docket api(TypeResolver typeResolver){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(DEFAULT_SWAGGER_PACKAGE))
                .build()
                .pathMapping("/")
                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false);
    }
}
