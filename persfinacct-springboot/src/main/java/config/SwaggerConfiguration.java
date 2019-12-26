package config;


import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket dynamicDocs() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/v1/personFinancialAccounts/**"))
                .build()
                .apiInfo(swaggerStaticApiInfo())
                .consumes(Sets.newHashSet("application/json"))
                .produces(Sets.newHashSet("application/json"))
                .directModelSubstitute(Throwable.class, PersonFinancialAccount.class)
                .directModelSubstitute(StackTraceElement.class, PersonFinancialAccount.class);
    }

    private ApiInfo swaggerStaticApiInfo() {
        return new ApiInfoBuilder()
                .description("* A Person Financial Account resource represents the financial summary of the Person or Payer owner.")
                .title("Person Financial Account")
                .version("1.0.0")
                .build();
    }


}
