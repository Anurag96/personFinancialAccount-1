package processor;

import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;

public interface PostProcessor {

    PersonFinancialAccount addLinks(PersonFinancialAccount pfa);

}
