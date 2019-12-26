package config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Slf4j
@Component
@ConfigurationProperties(prefix = "com.esrx.pfa.api")
@Data
public class SecureKafkaConfig {

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${com.esrx.pfa.api.kafkaKrbConfFile}")
    String kafkaKrbConfFile;

    @Value("${com.esrx.pfa.api.jaasConfigFile}")
    String jaasConfigFile;

    String trustStoreFile;

    String trustStorePassword;

    @Value("${spring.cloud.stream.kafka.binder.configuration.security.protocol}")
    String securityProtocol;

    @PostConstruct
    public void setProperty() {
        if (!"PLAINTEXT".equals(securityProtocol)) {
            ClassLoader classLoader = this.getClass().getClassLoader();
            File krbconfFile = new File(classLoader.getResource(kafkaKrbConfFile).getFile());
            if (krbconfFile.exists()) {
                System.setProperty("java.security.krb5.conf", krbconfFile.getAbsolutePath());
            }
            File jaasConfig = new File(classLoader.getResource(jaasConfigFile).getFile());
            System.setProperty("java.security.auth.login.config", jaasConfig.getAbsolutePath());
            System.setProperty("javax.security.auth.useSubjectCredsOnly", "true");

            File trustStore = new File(classLoader.getResource(trustStoreFile).getFile());
            System.setProperty("spring.cloud.stream.kafka.binder.configuration.ssl.truststore.location", trustStore.getAbsolutePath());
            System.setProperty("spring.cloud.stream.kafka.binder.configuration.ssl.truststore.password", trustStorePassword);
        }
    }
}