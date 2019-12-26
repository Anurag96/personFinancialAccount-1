package repositories.impl;

import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.models.SearchParameter;
import com.esrx.services.personfinancialaccounts.repositories.PFACustomRepository;
import com.esrx.services.personfinancialaccounts.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class PFACustomRepositoryImpl implements PFACustomRepository {

    private final MongoTemplate mongoTemplate;
    private final Utils utils;

    @Autowired
    public PFACustomRepositoryImpl(MongoTemplate mongoTemplate, Utils utils) {
        this.mongoTemplate = mongoTemplate;
        this.utils = utils;
    }

    @Override
    public List<PersonFinancialAccountDto> search(SearchParameter searchParameter) {
        Query query = new Query();
        query.addCriteria(utils.buildSearchCriteria(searchParameter));
        log.info("Query Criteria ::  {}", query);
        return mongoTemplate.find(query, PersonFinancialAccountDto.class);
    }
}
