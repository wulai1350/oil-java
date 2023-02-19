package com.rzico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;



@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private List<Parameter> globalParameters(){
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("Authorization").description("令牌")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数

        return pars;
    }


    @Bean
    public Docket pub_api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/*/*")).build().groupName("前端接口文档(无认证)V4.0").pathMapping("/")
                .globalOperationParameters(globalParameters())
                .apiInfo(apiInfo("前端接口文档(无认证)V4.0","所有接口无需登录后才能访问","4.0"));
    }

    @Bean
    public Docket web_api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/member/**")).build().groupName("前端接口文档(需认证)V4.0").pathMapping("/")
                .globalOperationParameters(globalParameters())
                .apiInfo(apiInfo("前端接口文档(需认证)V4.0","所有接口需登录后才能访问","4.0"));
    }

    @Bean
    public Docket game_api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/game/**")).build().groupName("游戏接口API(IP权授)V1.0").pathMapping("/")
                .globalOperationParameters(globalParameters())
                .apiInfo(apiInfo("游戏接口API(IP权授)V1.0","币积及发券相关接口","4.4"));
    }

    @Bean
    public Docket admin_api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/admin/**")).build().groupName("后台接口文档(需认证)V4.0").pathMapping("/")
                .globalOperationParameters(globalParameters())
                .apiInfo(apiInfo("后台接口文档(需认证)V4.0","所有接口需登录后才能访问","4.4"));
    }

    @Bean
    public Docket report_api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/report/**")).build().groupName("报表接口文档(需认证)V4.0").pathMapping("/")
                .globalOperationParameters(globalParameters())
                .apiInfo(apiInfo("报表接口文档(需认证)V4.0","所有接口需登录后才能访问","4.4"));
    }

    private ApiInfo apiInfo(String name,String description,String version) {
        ApiInfo apiInfo = new ApiInfoBuilder().title(name).description(description).version(version).build();
        return apiInfo;
    }

}
