package repositories;

import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.express_scripts.inf.logging.Loggable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Loggable
public interface PersonFinancialAccountRepository extends MongoRepository<PersonFinancialAccountDto, UUID> {

}
