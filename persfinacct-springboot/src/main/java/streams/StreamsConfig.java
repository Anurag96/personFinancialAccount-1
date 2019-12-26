package streams;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.event.ListenerContainerIdleEvent;

@EnableBinding(PFAStreams.class)
@Configuration
public class StreamsConfig {
    @Bean
    public ApplicationListener<ListenerContainerIdleEvent> idleListener() {
        return event -> {
            if (event.getConsumer().paused().isEmpty()) {
                event.getConsumer().resume(event.getConsumer().paused());
            }
        };
    }

}
