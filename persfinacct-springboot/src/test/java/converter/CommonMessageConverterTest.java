package converter;

import com.esrx.platforms.entity.common.datasync.model.message.Message;
import com.esrx.services.personfinancialaccounts.config.DateConfig;
import com.esrx.services.personfinancialaccounts.config.PersonFinancialAccountApplicationConfig;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DateConfig.class, PersonFinancialAccountApplicationConfig.class})
public class CommonMessageConverterTest {

    @Autowired
    private CommonMessageConverter commonMessageConverter;

    private String testJson = "{\"processorError\":null,\"context\":null,\"messageVersion\":1,\"header\":{\"parentId\":null,\"operation\":\"UPDATE\",\"payloadType\":null,\"originalSource\":\"NRXCUS\",\"sourceDateTime\":\"2019-10-21T21:17:54.230\",\"publisher\":\"NRX\",\"publisherType\":null,\"transactionId\":\"0000000078E3073C5412000000000100\",\"tenantId\":null,\"requestId\":null},\"payload\":{\"resourceId\":\"e26324e4-08c0-4ccd-ac23-7aa2add9702a\",\"createdDateTime\":\"2017-12-31T00:00:00Z\",\"updatedDateTime\":null,\"accountHolder\":null,\"currentBalance\":{\"amount\":90.00,\"currency\":\"USD\"},\"totalChargesInProcess\":{\"amount\":0.00,\"currency\":\"USD\"},\"totalPaymentsInProcess\":null,\"totalCreditsInProcess\":null,\"agingBalances\":[{\"daysAged\":\"30\",\"amount\":{\"amount\":50.00,\"currency\":\"USD\"}},{\"daysAged\":\"60\",\"amount\":{\"amount\":25.00,\"currency\":\"USD\"}},{\"daysAged\":\"90\",\"amount\":{\"amount\":15.00,\"currency\":\"USD\"}}],\"oldestCredit\":{\"creditAmount\":{\"amount\":0.00,\"currency\":\"USD\"},\"creditDate\":null},\"collectionLetters\":null,\"lastStatementDate\":null,\"currentFloorLimit\":{\"baseLimit\":{\"amount\":150.00,\"currency\":\"USD\"},\"highCostLimit\":{\"amount\":150.00,\"currency\":\"USD\"}},\"personalCeilingLimit\":null,\"debtThresholdExceededAsOf\":null,\"creditorEvents\":null,\"collectionAgencies\":null,\"extendedPaymentPlanParticipant\":null,\"status\":{\"effectiveDateTime\":null,\"value\":\"0\"},\"storeKeySet\":{\"primaryKey\":null,\"secondaryKeys\":[{\"storeName\":\"HomeDelivery\",\"keyName\":\"C-M-G\",\"keyComponents\":[{\"name\":\"CustomerNumber\",\"value\":\"710556462NPY\"},{\"name\":\"MailGroup\",\"value\":\"BBD\"},{\"name\":\"SubGroup\",\"value\":\"1192BBD\"}],\"status\":null},{\"storeName\":\"HomeDelivery\",\"keyName\":\"AGN\",\"keyComponents\":[{\"name\":\"Individual-AGN-ID\",\"value\":\"0\"}],\"status\":null},{\"storeName\":\"HomeDelivery\",\"keyName\":\"M\",\"keyComponents\":[{\"name\":\"MembershipId\",\"value\":\"0314459302\"}],\"status\":null}]}}}";

    @Test
    public void convertToMessage() {
        try {
            Message<PersonFinancialAccount> result = commonMessageConverter.convertToMessage(testJson);
            assertNotNull(result);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

}