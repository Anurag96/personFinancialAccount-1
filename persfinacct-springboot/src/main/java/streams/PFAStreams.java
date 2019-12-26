package streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

import static com.esrx.services.personfinancialaccounts.util.Constants.PFA_CHANGE;
import static com.esrx.services.personfinancialaccounts.util.Constants.PFA_LATEST;
import static com.esrx.services.personfinancialaccounts.util.Constants.PFA_SYNC;

@Service
public interface PFAStreams {

    @Output(PFA_LATEST)
    MessageChannel getPFALatestTopicChannel();

    @Input(PFA_SYNC)
    SubscribableChannel getPFASyncTopicChannel();

    @Output(PFA_CHANGE)
    MessageChannel getPFAChangeTopicChannel();
}
