package processor.impl;

import com.esrx.platforms.entity.common.datasync.model.message.Message;
import com.esrx.services.personfinancialaccounts.model.ChangePersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.processors.PFAProcessor;
import com.esrx.services.personfinancialaccounts.streams.PFAWriter;
import com.esrx.services.personfinancialaccounts.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import processor.PFAProcessor;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@RefreshScope
public class PFAProcessorImpl implements PFAProcessor {
    private final PFAWriter pfaWriter;
    private final Utils utils;

    @Override
    public void processPFAEvent(Message<ChangePersonFinancialAccount> message) {
        PersonFinancialAccount beforeData = message.getPayload().getBeforeData();
        PersonFinancialAccount afterData = message.getPayload().getData();
        UUID resourceId = (beforeData == null) ? afterData.getResourceId() : beforeData.getResourceId();

        log.debug("sendMsgToLatestTopic :: Start");
        pfaWriter.sendMsgToLatestTopic(utils.prepareLatestMsg(message), resourceId);
        log.debug("sendMsgToLatestTopic :: End");

        log.debug("sendMsgToChangeTopic :: Start");
        pfaWriter.sendMsgToChangeTopic(message, resourceId);
        log.debug("sendMsgToChangeTopic :: End");

    }
}
