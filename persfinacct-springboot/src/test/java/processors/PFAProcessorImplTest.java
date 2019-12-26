package processors;

import com.esrx.platforms.entity.common.datasync.model.message.Header;
import com.esrx.platforms.entity.common.datasync.model.message.Message;
import com.esrx.services.personfinancialaccounts.model.ChangePersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.monitor.MeterRegistryHandler;
import com.esrx.services.personfinancialaccounts.processors.impl.PFAProcessorImpl;
import com.esrx.services.personfinancialaccounts.streams.PFAStreams;
import com.esrx.services.personfinancialaccounts.streams.PFAWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class PFAProcessorImplTest {
	@Mock
	private PFAStreams streams;

	@Mock
	private MessageChannel PFA_LATEST;

	@Mock
	private MeterRegistryHandler registryHandler;

	@Mock
	@Qualifier("serializingObjectMapper")
	private ObjectMapper objectMapper;

    @Mock
	private PFAProcessorImpl pfaProcessor;


	@Mock
	private ObjectWriter objectWriter;

	@Captor
	private ArgumentCaptor<org.springframework.messaging.Message<PersonFinancialAccount>> messageCaptor;

	@Mock
	private PFAWriter writer;


	@Mock
	private Header header;

	private byte[] rawMessage;
    @InjectMocks
	private PFAProcessorImpl processor;

	private PersonFinancialAccount personFinancialAccount;
	private UUID resourceId;

	@Mock
	private Message<PersonFinancialAccount> message;

	@Mock
	private Message<ChangePersonFinancialAccount> changePersonFinancialAccountMessage;

	private Long tenantId;
	private String orderResourceId;
	private String transactionId;
	private String requestId;
	private String eventAsString;
	private List<PersonFinancialAccount> shipmentList;

	@Test(expected = NullPointerException.class)
	public void testProcesspfaNullPointException() {
		processor.processPFAEvent(changePersonFinancialAccountMessage);
	}


}
