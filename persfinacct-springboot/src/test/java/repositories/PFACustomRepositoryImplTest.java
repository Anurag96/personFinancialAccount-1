package repositories;

import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.models.SearchParameter;
import com.esrx.services.personfinancialaccounts.repositories.impl.PFACustomRepositoryImpl;
import com.esrx.services.personfinancialaccounts.transformer.PFABOTransformer;
import com.esrx.services.personfinancialaccounts.util.TestUtils;
import com.esrx.services.personfinancialaccounts.util.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PFACustomRepositoryImplTest {

    @Mock
    MongoTemplate mongoTemplate;

    @Spy
    Utils utils = new Utils();

    @InjectMocks
    PFACustomRepositoryImpl pfaCustomRepositoryImpl;

    @Mock
    private PFABOTransformer pfaboTransformer;


    @Test
    public void testSearch() {
        List<PersonFinancialAccountDto> expected = Arrays.asList(TestUtils.createFinAccObject()).stream().map(pfaboTransformer::getPersonFinancialAccountDto)
                .collect(Collectors.toList());
        when(mongoTemplate.find(any(Query.class), any(Class.class))).thenReturn(expected);
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.setPersonResourceId(utils.getUUID());
        List<PersonFinancialAccountDto> personFinancialAccountDtos = pfaCustomRepositoryImpl.search(searchParameter);
        assertEquals(personFinancialAccountDtos, expected);
    }
}
