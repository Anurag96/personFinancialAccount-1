package services;

import com.esrx.platforms.entity.common.datasync.model.message.Header;
import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.models.SearchParameter;

import java.util.List;
import java.util.UUID;

public interface PersonFinancialAccountService {

    List<PersonFinancialAccount> findAll(SearchParameter searchParameter);

    PersonFinancialAccount findOne(UUID resourceId);

    PersonFinancialAccount insert(PersonFinancialAccount personFinancialAccounts, Header header);

    PersonFinancialAccount update(PersonFinancialAccountDto pfaBeforeChange, PersonFinancialAccount incomingPFADetails, Header header);

    void delete(PersonFinancialAccountDto existingPFADto, UUID resourceId, Header header);

}
