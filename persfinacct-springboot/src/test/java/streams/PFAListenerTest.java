package streams;

import com.esrx.platforms.entity.common.datasync.model.message.Header;
import com.esrx.platforms.entity.common.datasync.model.message.Message;
import com.esrx.services.personfinancialaccounts.converter.CommonMessageConverter;
import com.esrx.services.personfinancialaccounts.dto.CollectionAgencyDto;
import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.processors.PFAProcessor;
import com.esrx.services.personfinancialaccounts.repositories.PFACustomRepository;
import com.esrx.services.personfinancialaccounts.services.PersonFinancialAccountService;
import com.esrx.services.personfinancialaccounts.transformer.PFABOTransformer;
import com.esrx.services.personfinancialaccounts.transformer.PFADTOTransformer;
import com.esrx.services.personfinancialaccounts.util.TestUtils;
import com.esrx.services.personfinancialaccounts.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class PFAListenerTest {

    @Mock
    private PFAProcessor pFAProcessor;

    @Mock
    private CommonMessageConverter commonMessageConverter;

    @InjectMocks
    private PFAListener listener;

    @Mock
    private Utils utils;

    @Mock
    private CollectionAgencyDto collectionAgencyDto;

    @Mock
    private PersonFinancialAccountService service;

    @Mock
    private PFACustomRepository customRepository;

    @Mock
    private PFADTOTransformer pfadtoTransformer;

    @Mock
    private PFABOTransformer pfaboTransformer;

    private Message<PersonFinancialAccount> message;
    private PersonFinancialAccount pfaDetails;
    private PersonFinancialAccountDto pfaDto;

    @Before
    public void setUp() {
        pfaDetails = new PersonFinancialAccount();
        pfaDto = new PersonFinancialAccountDto();
        pfaDto.setResourceId(UUID.randomUUID());
        pfaDetails.setResourceId(pfaDto.getResourceId());
    }

    private byte[] getSampleMessage(String operationType) {
        Header header;
        byte[] rawMessage = null;
        message = new Message<>();
        header = new Header();
        header.setOperation(operationType);
        header.setSourceDateTime("2018-11-14T15:25:41.856");
        message.setHeader(header);
        message.setPayload(pfaDetails);

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(message);
            oos.flush();
            rawMessage = bos.toByteArray();
        } catch (Exception e) {
            log.error("Error :: {}", e.getMessage());
        }
        return rawMessage;
    }

    @Test
    public void testProcessPfaAttemptInsert() throws IOException {
        byte[] rawMessage = getSampleMessage("INSERT");
        when(commonMessageConverter.convertToMessage(new String(rawMessage))).thenReturn(message);
        message.setPayload(TestUtils.addCMG(message.getPayload()));
        Header headerDetails = new Header();
        when(utils.getHeaderDetailsFromKafkaMessageHeader(any())).thenReturn(headerDetails);
        listener.processPfaSyncFills(rawMessage);
        verify(utils).getHeaderDetailsFromKafkaMessageHeader(any());
        verify(service).insert(pfaDetails, headerDetails);
    }

    @Test
    public void testProcessPfaAttemptDelete() throws IOException {
        byte[] rawMessage = getSampleMessage("DELETE");
        when(commonMessageConverter.convertToMessage(new String(rawMessage))).thenReturn(message);
        message.setPayload(TestUtils.addCMG(message.getPayload()));
        when(customRepository.search(any())).thenReturn(Arrays.asList(pfaDto));
        when(pfadtoTransformer.getPersonFinancialAccountBo(pfaDto)).thenReturn(pfaDetails);
        Header headerDetails = new Header();
        when(utils.getHeaderDetailsFromKafkaMessageHeader(any())).thenReturn(headerDetails);

        listener.processPfaSyncFills(rawMessage);
        verify(utils).getHeaderDetailsFromKafkaMessageHeader(any());
        verify(service).delete(pfaDto, pfaDto.getResourceId(), headerDetails);
    }

    @Test
    public void testProcessPfaAttemptUpdate() throws IOException {
        PersonFinancialAccountDto accDetailsDto = new PersonFinancialAccountDto();
        accDetailsDto.setCollectionAgencies(Arrays.asList(collectionAgencyDto));

        byte[] rawMessage = getSampleMessage("UPDATE");
        when(commonMessageConverter.convertToMessage(new String(rawMessage))).thenReturn(message);
        message.setPayload(TestUtils.addCMG(message.getPayload()));
        PersonFinancialAccount pfaAfterChange = message.getPayload();

        when(customRepository.search(any())).thenReturn(Arrays.asList(pfaDto));
        when(pfadtoTransformer.getPersonFinancialAccountBo(any())).thenReturn(pfaDetails);

        Header headerDetails = new Header();
        when(utils.getHeaderDetailsFromKafkaMessageHeader(any())).thenReturn(headerDetails);

        listener.processPfaSyncFills(rawMessage);

        verify(utils).getHeaderDetailsFromKafkaMessageHeader(any());
        verify(service).update(pfaDto, pfaDetails, headerDetails);

    }
}
