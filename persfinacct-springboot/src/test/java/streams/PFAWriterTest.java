package streams;

import com.esrx.platforms.entity.common.datasync.model.message.Header;
import com.esrx.platforms.entity.common.datasync.model.message.Message;
import com.esrx.services.personfinancialaccounts.model.ChangePersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.monitor.MeterRegistryHandler;
import com.esrx.services.personfinancialaccounts.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PFAWriterTest {
	@Mock
	private PFAStreams streams;

	@Mock
	private MessageChannel PFA_LATEST;

	@Mock
	private MessageChannel PFA_CHANGE;

	@Mock
	private MeterRegistryHandler registryHandler;

	@Mock
	@Qualifier("serializingObjectMapper")
	private ObjectMapper objectMapper;

    @Mock
    private Utils utils;

	@Mock
	private ObjectWriter objectWriter;

	@Captor
	private ArgumentCaptor<org.springframework.messaging.Message<PersonFinancialAccount>> messageCaptor;

	@InjectMocks
	private PFAWriter writer;

	private Message<PersonFinancialAccount> message;
	private Message<ChangePersonFinancialAccount> changePFAMessage;
	private Header header;
	private byte[] rawMessage;

	private PersonFinancialAccount personFinancialAccount;

	private String eventAsString;
	private String key;
	private String requestId;

	@SuppressWarnings("static-access")
	@Before
	public void setUp() {

		personFinancialAccount = new PersonFinancialAccount();

		message = new Message<>();
		header = new Header();
		header.setOperation("Insert");
		header.setSourceDateTime("2018-11-14T15:25:41.856");
		message.setHeader(header);
		message.setPayload(personFinancialAccount);

		toWriteObject(message);

		when(streams.getPFALatestTopicChannel()).thenReturn(PFA_LATEST);

		when(objectMapper.writer()).thenReturn(objectWriter);

		ChangePersonFinancialAccount changePersonFinancialAccount = new ChangePersonFinancialAccount(null, personFinancialAccount);
		changePFAMessage = new Message<>();
		changePFAMessage.setHeader(header);
		changePFAMessage.setPayload(changePersonFinancialAccount);
		toWriteObject(changePFAMessage);

		when(streams.getPFAChangeTopicChannel()).thenReturn(PFA_CHANGE);

	}

	private<T> void toWriteObject(T obj) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			rawMessage = bos.toByteArray();
		} catch (Exception e) {

		}
	}

	@Test
	public void sendMsgToLatestTopicTest() throws Exception {
		eventAsString = "{ \"header\" :{\"operation\":\"insert\"},\"payload\":{\"resourceId\":\"e26324e4-08c0-4ccd-ac23-7aa2add9702a\", \"storeKeySet\":{\"primaryKey\":null,\"secondaryKeys\":[{\"storeName\":\"HomeDelivery\",\"keyName\":\"C-M-G\",\"keyComponents\":[{\"name\":\"CustomerNumber\",\"value\":\"190556462NPY\"},{\"name\":\"MailGroup\",\"value\":\"AAD\"},{\"name\":\"SubGroup\",\"value\":\"2292BBD\"}],\"status\":null},{\"storeName\":\"HomeDelivery\",\"keyName\":\"AGN\",\"keyComponents\":[{\"name\":\"Individual-AGN-ID\",\"value\":\"0\"}],\"status\":null},{\"storeName\":\"HomeDelivery\",\"keyName\":\"M\",\"keyComponents\":[{\"name\":\"MembershipId\",\"value\":\"0314459302\"}],\"status\":null}]}}}";
		when(objectWriter.writeValueAsString(message)).thenReturn(eventAsString);

		UUID resourceId = UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a");
		message.getPayload().setResourceId(resourceId);

		writer.sendMsgToLatestTopic(message, resourceId);
		verify(PFA_LATEST).send(messageCaptor.capture());
		assertEquals(1, messageCaptor.getAllValues().size());
		assertEquals(eventAsString, messageCaptor.getValue().getPayload());
	}

	@Test(expected = IllegalArgumentException.class)
	public void sendMsgToLatestTopicPayloadNullTest() throws Exception {
		UUID resourceId = UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a");
		when(objectWriter.writeValueAsString(message)).thenReturn(eventAsString);
		message.getPayload().setResourceId(resourceId);
		writer.sendMsgToLatestTopic(message, resourceId);
		verify(PFA_LATEST, never()).send(any());
	}

	@Test
	public void sendMsgToLatestTopicNullMessageTest() throws Exception {
		UUID resourceId = UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a");

		writer.sendMsgToLatestTopic(null, resourceId);
		verify(PFA_LATEST, never()).send(any());
	}

	@Test
	public void sendMsgToChangeTopicTest() throws Exception {
		eventAsString = "{ \"header\" :{\"operation\":\"insert\"},\"payload\":{\"resourceId\":\"e26324e4-08c0-4ccd-ac23-7aa2add9702a\", \"storeKeySet\":{\"primaryKey\":null,\"secondaryKeys\":[{\"storeName\":\"HomeDelivery\",\"keyName\":\"C-M-G\",\"keyComponents\":[{\"name\":\"CustomerNumber\",\"value\":\"190556462NPY\"},{\"name\":\"MailGroup\",\"value\":\"AAD\"},{\"name\":\"SubGroup\",\"value\":\"2292BBD\"}],\"status\":null},{\"storeName\":\"HomeDelivery\",\"keyName\":\"AGN\",\"keyComponents\":[{\"name\":\"Individual-AGN-ID\",\"value\":\"0\"}],\"status\":null},{\"storeName\":\"HomeDelivery\",\"keyName\":\"M\",\"keyComponents\":[{\"name\":\"MembershipId\",\"value\":\"0314459302\"}],\"status\":null}]}}}";
		when(objectWriter.writeValueAsString(changePFAMessage)).thenReturn(eventAsString);

		UUID resourceId = UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a");

		writer.sendMsgToChangeTopic(changePFAMessage, resourceId);
		verify(PFA_CHANGE).send(messageCaptor.capture());
		assertEquals(1, messageCaptor.getAllValues().size());
		assertEquals(eventAsString, messageCaptor.getValue().getPayload());
	}

	@Test(expected = IllegalArgumentException.class)
	public void sendMsgToChangeTopicPayloadNullTest() throws Exception {
		UUID resourceId = UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a");
		when(objectWriter.writeValueAsString(changePFAMessage)).thenReturn(eventAsString);

		writer.sendMsgToChangeTopic(changePFAMessage, resourceId);
		verify(PFA_CHANGE, never()).send(any());
	}

	@Test
	public void sendMsgToChangeTopicNullMessageTest() throws Exception {
		UUID resourceId = UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a");

		writer.sendMsgToChangeTopic(null, resourceId);
		verify(PFA_CHANGE, never()).send(any());
	}

}
