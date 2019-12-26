package monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class MeterRegistryHandlerTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    @Mock
    private Timer timer;

    @InjectMocks
    private MeterRegistryHandler registryHandler;

    @Test
    public void incrementCounter() {
        when(meterRegistry.counter(any(String.class), any(Iterable.class))).thenReturn(counter);
        registryHandler.incrementCounter("someCounterName");
        verify(meterRegistry).counter(any(), any(Tags.class));
    }

    @Test
    public void recordExecutionTime(){
        when(meterRegistry.timer(any(String.class), any(Iterable.class))).thenReturn(timer);
        registryHandler.recordExecutionTime("eventName",20L);
        verify(meterRegistry).timer(any(), any(Tags.class));
    }
}