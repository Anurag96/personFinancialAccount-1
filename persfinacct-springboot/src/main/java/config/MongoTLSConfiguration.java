package config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.util.Optional;

@Configuration
@ConditionalOnProperty(name = "mongo.ssl.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "mongo")
@Data
public class MongoTLSConfiguration {

    private String mongoSslKeyStorePassword;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    private String env;

    private String keyStoreFileName;

    private KeyStore loadKeyStore(File keyStore, String keyStoreType, String keyStorePassword) throws Exception {
        KeyStore ks;
        try (InputStream in = new FileInputStream(keyStore)) {
            ks = KeyStore.getInstance(keyStoreType);
            ks.load(in, keyStorePassword.toCharArray());
        }
        return ks;
    }

    private SSLContext getMongoDbSslContext() throws Exception {
        KeyManagerFactory kmf;

        URL mongoSslKeyStoreLocator = Optional.ofNullable(getClass().getClassLoader().getResource("./config/" + env + "/" + keyStoreFileName))
                .orElseThrow(() -> new RuntimeException("MONGODB KEYSTORE FILE NOT FOUND"));

        File mongoSslKeyStore = new File(mongoSslKeyStoreLocator.toURI());

        KeyStore ks = loadKeyStore(mongoSslKeyStore, "jks", mongoSslKeyStorePassword);
        kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, mongoSslKeyStorePassword.toCharArray());

        SSLContext context = SSLContext.getInstance("TLS");
        KeyManager[] kmArray = kmf.getKeyManagers();
        context.init(kmArray, null, null);
        return context;
    }

    private MongoClientOptions.Builder getMongoClientOptionsBuilder() throws Exception {
        MongoClientOptions.Builder builder = MongoClientOptions.builder();
        builder.sslEnabled(true);
        builder.socketFactory(getMongoDbSslContext().getSocketFactory());
        return builder;
    }

    @Bean
    public MongoClient mongoClient() throws Exception {
        MongoClientURI mongoClientURI = new MongoClientURI(mongoUri, getMongoClientOptionsBuilder());
        return new MongoClient(mongoClientURI);
    }
}