package processor;

import com.esrx.platforms.entity.common.datasync.model.message.Message;
import com.esrx.services.personfinancialaccounts.model.ChangePersonFinancialAccount;


public interface PFAProcessor {
    void processPFAEvent(Message<ChangePersonFinancialAccount> message);
}
