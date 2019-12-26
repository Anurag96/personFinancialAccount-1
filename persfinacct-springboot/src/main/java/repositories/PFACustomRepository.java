package repositories;

import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.models.SearchParameter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PFACustomRepository {

    List<PersonFinancialAccountDto> search(SearchParameter searchParameter);
}
