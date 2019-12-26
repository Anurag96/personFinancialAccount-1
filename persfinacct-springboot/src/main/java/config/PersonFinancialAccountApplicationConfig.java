package config;

import com.esrx.services.personfinancialaccounts.converter.CommonMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonFinancialAccountApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CommonMessageConverter commonMessageConverter(@Qualifier("serializingObjectMapper") ObjectMapper mapper) {
        return new CommonMessageConverter(mapper);
    }

}
