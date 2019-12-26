package streams;

import com.esrx.platforms.entity.common.datasync.model.message.Header;
import com.esrx.platforms.entity.common.datasync.model.message.Message;
import com.esrx.services.personfinancialaccounts.converter.CommonMessageConverter;
import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.exception.MessagesListenerException;
import com.esrx.services.personfinancialaccounts.model.KeyComponent;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.StoreKey;
import com.esrx.services.personfinancialaccounts.models.SearchParameter;
import com.esrx.services.personfinancialaccounts.processors.PFAProcessor;
import com.esrx.services.personfinancialaccounts.repositories.PFACustomRepository;
import com.esrx.services.personfinancialaccounts.services.PersonFinancialAccountService;
import com.esrx.services.personfinancialaccounts.transformer.PFADTOTransformer;
import com.esrx.services.personfinancialaccounts.util.Constants;
import com.esrx.services.personfinancialaccounts.util.Utils;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.esrx.services.personfinancialaccounts.util.Constants.PFA_SYNC;

@Component
@RequiredArgsConstructor
@Slf4j
public class PFAListener {

    private final CommonMessageConverter commonMessageConverter;

    private final Utils utils;

    private final PersonFinancialAccountService personFinancialAccountService;

    private final PFACustomRepository customRepository;

    private final PFADTOTransformer pfadtoTransformer;

    private final PFAProcessor pfaProcessor;

    private KeyFields getKeyFields(List<StoreKey> storekeyList) {
        KeyFields keyFields = new KeyFields();
        if (CollectionUtils.isNotEmpty(storekeyList)) {
            for (StoreKey storekey : storekeyList) {
                if (!ObjectUtils.isEmpty(storekey) && Constants.CMG.equals(storekey.getKeyName())) {
                    setKeyValues(storekey, keyFields);
                }
            }
        }
        log.debug("keyFields : {}", keyFields);
        return keyFields;
    }

    private void setKeyValues(StoreKey storekey, KeyFields keyFields) {
        for (KeyComponent keyComponent : storekey.getKeyComponents()) {
            if (keyComponent.getName().equalsIgnoreCase(Constants.CUSTOMER_NUMBER_KEY))
                keyFields.setCustomerNumber(keyComponent.getValue());
            if (keyComponent.getName().equalsIgnoreCase(Constants.MAIL_GROUP_KEY))
                keyFields.setMailGroup(keyComponent.getValue());
            if (keyComponent.getName().equalsIgnoreCase(Constants.SUB_GROUP_KEY))
                keyFields.setSubGroup(keyComponent.getValue());
        }
    }

    @StreamListener(PFA_SYNC)
    public void processPfaSyncFills(byte[] rawMessage) {
        log.debug(PFA_SYNC + " RAW MESSAGE CONSUMED");

        String messageAsString = new String(rawMessage);
        log.debug("Message received = '{}'", messageAsString);
        Message<PersonFinancialAccount> message = convertToMessage(messageAsString);
        if (!ObjectUtils.isEmpty(message)) {
            handleMessages(message);
        }
    }

    private void handleMessages(Message<PersonFinancialAccount> message) {
        Header header = message.getHeader();
        String operation = (!ObjectUtils.isEmpty(header) && !ObjectUtils.isEmpty(header.getOperation())) ? header.getOperation().toUpperCase() : Constants.UNKNOWN;
        PersonFinancialAccount incomingPFADetails = message.getPayload();
        KeyFields keyFields = getKeyFields(incomingPFADetails.getStoreKeySet().getSecondaryKeys());

        if (!isOperationSupported(operation, message) || !isValidCMG(keyFields, message)) {
            return; //Cannot continue if there is no valid operation or Customer, mailGroup, SubGroup information missing
        }

        Header headerDetails = utils.getHeaderDetailsFromKafkaMessageHeader(header);
        List<PersonFinancialAccountDto> pfaList = customRepository.search(new SearchParameter(keyFields.getCustomerNumber(), keyFields.getMailGroup(), keyFields.getSubGroup()));

        if (CollectionUtils.isNotEmpty(pfaList)) {
            PersonFinancialAccountDto pfaBeforeChangeDto = pfaList.get(0);
            PersonFinancialAccount pfaBeforeChange = pfadtoTransformer.getPersonFinancialAccountBo(pfaBeforeChangeDto);

            UUID resourceId = pfaBeforeChange.getResourceId();
            if (Constants.DELETE.equals(operation)) {
                handleDeletePFA(headerDetails, pfaBeforeChangeDto, resourceId);
            } else {
                handleUpdatePFA(incomingPFADetails, headerDetails, pfaBeforeChangeDto, resourceId);
            }
            log.info("Operation completed Successfully: {}, Payload: {}", operation, message.getPayload());
        } else if (!Constants.DELETE.equals(operation)) {  //TO make sure that only insert or update goes in and delete will not insert anything new
            handleInsertPFA(incomingPFADetails, headerDetails);
            log.info("Operation completed Successfully: {}, Payload: {}", operation, message.getPayload());
        } else {
            log.error("*** Operation FAILED - No Record Found in DB to : {}, ResourceID: {}", operation, message.getPayload().getResourceId());
        }
    }

    @Timed(value = "java", extraTags = {"reportGroups", "kafkaInsertPFAProcessing"})
    private void handleInsertPFA(PersonFinancialAccount incomingPFADetails, Header header) {
        personFinancialAccountService.insert(incomingPFADetails, header);
    }

    @Timed(value = "java", extraTags = {"reportGroups", "kafkaUpdatePFAProcessing"})
    private void handleUpdatePFA(PersonFinancialAccount incomingPFADetails, Header header, PersonFinancialAccountDto pfaBeforeChangeDto, UUID resourceId) {
        //Incoming message will not have resource ID
        incomingPFADetails.setResourceId(resourceId);
        personFinancialAccountService.update(pfaBeforeChangeDto, incomingPFADetails, header);
    }

    @Timed(value = "java", extraTags = {"reportGroups", "kafkaDeletePFAProcessing"})
    private void handleDeletePFA(Header header, PersonFinancialAccountDto pfaBeforeChangeDto, UUID resourceId) {
        personFinancialAccountService.delete(pfaBeforeChangeDto, resourceId, header);
    }

    private Message<PersonFinancialAccount> convertToMessage(String messageAsString) {
        try {
            return commonMessageConverter.convertToMessage(messageAsString);
        } catch (IOException e) {
            log.error("Cannot parse message from string, messageAsString = {}", messageAsString);
            throw new MessagesListenerException("an error occurred converting a message");
        }
    }

    private boolean isOperationSupported(String operation, Message<PersonFinancialAccount> message) {
        for (AllowedOperations optn : AllowedOperations.values()) {
            if (optn.name().equals(operation)) {
                return true;
            }
        }
        log.info("Operation NOT supported: {}, Payload: {}", operation, message.getPayload());
        return false;
    }

    private boolean isValidCMG(KeyFields keyFields, Message<PersonFinancialAccount> message) {
        boolean result = StringUtils.isNotBlank(keyFields.getCustomerNumber()) && StringUtils.isNotBlank(keyFields.getMailGroup()) && StringUtils.isNotBlank(keyFields.getSubGroup());
        if (!result) {
            log.info("Missing/Invalid CMG Info: {}", message.getPayload());
        }
        return result;
    }

    enum AllowedOperations {
        INSERT, UPDATE, DELETE, REFRESH
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class KeyFields {
        private String customerNumber;
        private String mailGroup;
        private String subGroup;
    }

}
