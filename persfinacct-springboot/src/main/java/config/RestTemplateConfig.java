package config;

import com.esrx.services.core.web.resttemplate.RestTemplateFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Configuration
@Component
@ConfigurationProperties(prefix = "oauth.consumer")
@Getter
@Setter
public class RestTemplateConfig {

    private String requestTokenURL;
    private String accessTokenURL;
    private String consumerKey;
    private String consumerSecret;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = RestTemplateFactory.getEntityRestTemplate(requestTokenURL, accessTokenURL, consumerKey, consumerSecret);
        restTemplate.getInterceptors().add(new ResponseLoggingInterceptor());
        return restTemplate;
    }
}

