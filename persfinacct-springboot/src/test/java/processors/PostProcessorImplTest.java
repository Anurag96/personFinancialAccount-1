package processors;

import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.processors.impl.PostProcessorImpl;
import com.esrx.services.personfinancialaccounts.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
public class PostProcessorImplTest {

    @InjectMocks
    private PostProcessorImpl postProcessor;

    @Test
    public void testAddLinks() {
        PersonFinancialAccount accDetails = postProcessor.addLinks(TestUtils.createFinAccObject());
        assertNotEquals(accDetails.getLinks(), null);
    }
}
