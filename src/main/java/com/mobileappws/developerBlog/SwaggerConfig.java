package com.mobileappws.developerBlog;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@EnableSwagger2
@EnableWebMvc
@Component
public class SwaggerConfig {

    Contact contact = new Contact(
            "Raihan Uddin",
            "http://www.mobileappws.com",
            "raihanuddin561@gmail.com"
    );
    List<VendorExtension> vendorExtensions = new ArrayList<>();
    ApiInfo apiInfo = new ApiInfo(
            "RESTful Web Service Documentation",
            "This pages documents RESTful web service endpoints",
            "1.0",
            "example.service.com",
            contact
            ,
            "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0",
            vendorExtensions
    );
    @Bean
    public Docket apiDocket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .protocols(new HashSet<>(Arrays.asList("HTTP","HTTPs")))
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mobileappws.developerBlog"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
