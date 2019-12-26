package config;

import com.esrx.inf.spring.boot.autoconfigure.security.HttpSecurityConfigurer;
import com.express_scripts.inf.security.authentication.spring.jwt.JWTAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class WebSecurityConfiguration implements HttpSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll();
    }

}