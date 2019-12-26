package health;

import org.junit.Test;

import static org.junit.Assert.*;

public class AppHealthAggregatorTest {
    @Test
    public void aggregateStatus() throws Exception {
        AppHealthAggregator appHealthAggregator = new AppHealthAggregator();
        assertNull(appHealthAggregator.aggregateStatus(null));
    }

}