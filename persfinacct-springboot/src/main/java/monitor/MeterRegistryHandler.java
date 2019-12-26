package monitor;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MeterRegistryHandler {

    @Autowired
    private MeterRegistry meterRegistry;

    public void incrementCounter(String counterName) {
        meterRegistry.counter(counterName, Tags.empty()).increment();
    }

    public void recordExecutionTime(String eventName,Long transactionTime){
        meterRegistry.timer(eventName, Tags.empty()).record(transactionTime, TimeUnit.MILLISECONDS);
    }
}
