package health;

import org.springframework.boot.actuate.health.AbstractHealthAggregator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppHealthAggregator extends AbstractHealthAggregator{
    @Override
    protected Status aggregateStatus(List<Status> candidates) {
        return null;
    }
}
