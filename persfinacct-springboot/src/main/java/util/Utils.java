package util;

import com.esrx.platforms.entity.common.datasync.model.message.Header;
import com.esrx.platforms.entity.common.datasync.model.message.Message;
import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.dto.StatusDto;
import com.esrx.services.personfinancialaccounts.exception.ParametersRequiredException;
import com.esrx.services.personfinancialaccounts.model.ChangePersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.KeyComponent;
import com.esrx.services.personfinancialaccounts.model.OperationType;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.Status;
import com.esrx.services.personfinancialaccounts.model.StoreKey;
import com.esrx.services.personfinancialaccounts.models.SearchParameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public final class Utils {

    /**
     * generates a random UUID
     *
     * @return UUID String
     */
    public UUID getUUID() {
        return UUID.randomUUID();
    }

    public LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now();
    }

    /**
     * Converts String to UUID and validates it
     *
     * @param id
     * @param idType
     * @return UUID
     */
    public UUID getUUIDFromString(String id, String idType) {
        UUID uuid = null;
        if (StringUtils.isNotBlank(id)) {
            try {
                uuid = UUID.fromString(id);
            } catch (IllegalArgumentException ex) {
                throw new ParametersRequiredException("Please provide a valid " + idType + ".");
            }
        }
        return uuid;
    }

    /**
     * Utility method to create a prepareStore Key
     *
     * @param storeName
     * @param keyName
     * @param keyComponentName
     * @param keyComponentValue
     * @return StoreKey
     */
    public StoreKey prepareStoreKey(String storeName, String keyName, Status keyStatus, String keyComponentName, String keyComponentValue) {
        StoreKey storeKey = new StoreKey();
        storeKey.setStoreName(storeName);
        storeKey.setKeyName(keyName);
        storeKey.setStatus(keyStatus);

        storeKey.setKeyComponents(prepareKeyComponent(keyComponentName, keyComponentValue, storeKey.getKeyComponents()));
        return storeKey;
    }

    /**
     * Utility method to create a prepareStore Key
     *
     * @param storeName
     * @param keyName
     * @return StoreKey
     */
    public StoreKey prepareStoreKey(String storeName, String keyName, Status keyStatus, List<KeyComponent> keyComponents) {
        StoreKey storeKey = new StoreKey();
        storeKey.setStoreName(storeName);
        storeKey.setKeyName(keyName);
        storeKey.setStatus(keyStatus);
        storeKey.setKeyComponents(keyComponents);
        return storeKey;
    }

    /**
     * Utility method to prepare key component
     *
     * @param keyComponentName
     * @param keyComponentValue
     * @param keyComponents
     * @return List<KeyComponent>
     */
    public List<KeyComponent> prepareKeyComponent(String keyComponentName, String keyComponentValue, List<KeyComponent> keyComponents) {
        if (ObjectUtils.isEmpty(keyComponents)) {
            keyComponents = new ArrayList<>();
        }
        KeyComponent keyComponent = new KeyComponent();
        keyComponent.setName(keyComponentName);
        keyComponent.setValue(keyComponentValue);
        keyComponents.add(keyComponent);
        return keyComponents;
    }

    /**
     * Building Search Criteria
     *
     * @param searchParameter
     * @return Criteria
     */
    public Criteria buildSearchCriteria(SearchParameter searchParameter) {
        List<Criteria> criteriaList = new ArrayList<>();
        Criteria orCriteria = new Criteria();

        if (!ObjectUtils.isEmpty(searchParameter)) {
            if (searchParameter.getIndividualAGN() != null) {
                criteriaList.add(getAGNCriteria(searchParameter.getIndividualAGN()));
            }

            if (StringUtils.isNotBlank(searchParameter.getMembershipId())) {
                criteriaList.add(getMembershipIdCriteria(searchParameter.getMembershipId()));
            }

            if (StringUtils.isNotBlank(searchParameter.getCustomerNumber())) {
                criteriaList.addAll(getCMGCriteria(searchParameter.getCustomerNumber(), searchParameter.getMailGroup(), searchParameter.getSubGroup()));
            }

            if (!ObjectUtils.isEmpty(searchParameter.getPersonResourceId())) {
                criteriaList.add(getPersonResourceIdCriteria(searchParameter.getPersonResourceId()));
            }
        }

        return orCriteria.andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
    }

    /**
     * Prepares Criteria for AGN
     *
     * @param individualAGN
     * @return Criteria
     */
    public Criteria getAGNCriteria(String individualAGN) {
        Criteria criteria = new Criteria();
        criteria.and(Constants.KEY_COMPONENT_NAME_PATH).is(Constants.AGN_KEY);
        criteria.and(Constants.KEY_COMPONENT_VALUE_PATH).is(individualAGN);
        return criteria;
    }

    /**
     * Prepares Criteria for Cusomer, Mail Group and SubGroup
     *
     * @param customerNumber
     * @param mailGroup
     * @param subGroup
     * @return List<Criteria>
     */
    public List<Criteria> getCMGCriteria(String customerNumber, String mailGroup, String subGroup) {
        List<Criteria> criteriaList = new ArrayList<>();
        Criteria subCriteria1 = new Criteria();
        //Criteria for customerNumber field
        subCriteria1.and(Constants.KEY_COMPONENT_NAME_PATH).is(Constants.CUSTOMER_NUMBER_KEY);
        subCriteria1.and(Constants.KEY_COMPONENT_VALUE_PATH).is(customerNumber);
        criteriaList.add(subCriteria1);

        //Criteria for mailGroup field
        if (StringUtils.isNotBlank(mailGroup)) {
            Criteria subCriteria2 = new Criteria();
            subCriteria2.and(Constants.KEY_COMPONENT_NAME_PATH).is(Constants.MAIL_GROUP_KEY);
            subCriteria2.and(Constants.KEY_COMPONENT_VALUE_PATH).is(mailGroup);
            criteriaList.add(subCriteria2);
        }

        //Criteria for subGroup field
        if (StringUtils.isNotBlank(subGroup)) {
            Criteria subCriteria3 = new Criteria();
            subCriteria3.and(Constants.KEY_COMPONENT_NAME_PATH).is(Constants.SUB_GROUP_KEY);
            subCriteria3.and(Constants.KEY_COMPONENT_VALUE_PATH).is(subGroup);
            criteriaList.add(subCriteria3);
        }
        return criteriaList;
    }

    /**
     * Prepares Criteria for Membership Id
     *
     * @param membershipId
     * @return Criteria
     */
    public Criteria getMembershipIdCriteria(String membershipId) {
        Criteria criteria = new Criteria();
        criteria.and(Constants.KEY_COMPONENT_NAME_PATH).is(Constants.MEMBERSHIP_ID_KEY);
        criteria.and(Constants.KEY_COMPONENT_VALUE_PATH).is(membershipId);
        return criteria;
    }

    /**
     * Prepares Criteria for Person Resource Id
     *
     * @param personResourceId
     * @return Criteria
     */
    public Criteria getPersonResourceIdCriteria(UUID personResourceId) {
        Criteria criteria = new Criteria();
        criteria.and(Constants.ACCOUNT_HOLDER_KEY_PATH).is(personResourceId);
        return criteria;
    }

    public Message<ChangePersonFinancialAccount> prepareChangeMessage(Header header, PersonFinancialAccount pfaBeforeChange, PersonFinancialAccount pfaAfterChange) {
        Message<ChangePersonFinancialAccount> changeMsg = new Message<>();
        changeMsg.setPayload(new ChangePersonFinancialAccount(pfaBeforeChange, pfaAfterChange));
        changeMsg.setHeader(header);
        return changeMsg;
    }

    public Message<PersonFinancialAccount> prepareLatestMsg(Message<ChangePersonFinancialAccount> chngMessage) {
        Message<PersonFinancialAccount> latestMessage = new Message<>();
        latestMessage.setPayload(chngMessage.getPayload().getData() == null ? null : chngMessage.getPayload().getData());
        latestMessage.setHeader(chngMessage.getHeader());
        return latestMessage;
    }

    public void loadHeaderDetails(Header header, PersonFinancialAccountDto personFinancialAccountDto) {
        if (header != null && personFinancialAccountDto != null) {
            personFinancialAccountDto.setTenantId(header.getTenantId());
        }
    }

    public Header getHeaderDetailsFromHTTPRequest(Map<String, String> headerMap, OperationType operationType) {
        log.info("headerMap :{}", headerMap);
        Header header = new Header();
        String tenantIdStr = headerMap.get(Constants.TENANT_ID);
        try {
            header.setTenantId(StringUtils.isNotBlank(tenantIdStr) ? Long.parseLong(tenantIdStr) : null);
        } catch (Exception e) {
            log.error("Invalid Tenant Id: {}, error :{} ", tenantIdStr, e.getMessage());
        }

        String transactionId = headerMap.get(Constants.TRANSACTION_ID);
        if (StringUtils.isBlank(transactionId)) {
            transactionId = getUUID().toString();
            log.debug("********* Generating Transaction Id :: {}", transactionId);
        }
        header.setTransactionId(transactionId);
        header.setOriginalSource(Constants.PUBLISHER);
        header.setPublisher(Constants.PUBLISHER);
        header.setSourceDateTime(getCurrentDateAsString());
        if (operationType != null) {
            header.setOperation(operationType.name());
        }
        return header;
    }

    public Header getHeaderDetailsFromKafkaMessageHeader(Header header) {
        Header newHeader = new Header();
        if (header != null) {
            String transactionId = header.getTransactionId();
            if (StringUtils.isBlank(transactionId)) {
                transactionId = getUUID().toString();
                log.debug("********* Generating Transaction Id :: {}", transactionId);
            }
            newHeader.setTransactionId(transactionId);
            newHeader.setOriginalSource(header.getOriginalSource());
            newHeader.setSourceDateTime(header.getSourceDateTime());
            newHeader.setSourceTableName(header.getSourceTableName());
            newHeader.setOperation(header.getOperation());
            newHeader.setTenantId(header.getTenantId());
            newHeader.setPublisher(Constants.PUBLISHER);
        }
        return newHeader;
    }

    public String getCurrentDateAsString() {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public PersonFinancialAccountDto mergePFADetails(PersonFinancialAccountDto existingPFADto, PersonFinancialAccountDto incomingPFADto) {
        if (existingPFADto != null && incomingPFADto != null) {
            List<StatusDto> statusDtoList = new ArrayList<>();
            incomingPFADto.setCreatedDateTime(existingPFADto.getCreatedDateTime());
            if (CollectionUtils.isNotEmpty(existingPFADto.getStatusList())) {
                //Sorting the status list
                statusDtoList = existingPFADto.getStatusList().stream().sorted(Comparator.comparing(StatusDto::getEffectiveDateTime)).collect(Collectors.toList());
            }

            //Getting latest status to make sure we are not duplicating the status upon reprocess.
            List<StatusDto> incomingStatusList = incomingPFADto.getStatusList();

            //We have explicitly add the old status history to maintain in the database and making sure the last status is not we are trying to add again
            if (CollectionUtils.isNotEmpty(incomingStatusList) && !ObjectUtils.isEmpty(incomingStatusList.get(0))) {
                StatusDto newStatus = incomingStatusList.get(0);
                //This is to maintain the status history in case if there is a replay of messages happen.. this will make sure that the same status is not inserted twice
                List<StatusDto> filteredList = statusDtoList.stream().filter(li -> !li.getEffectiveDateTime().isAfter(newStatus.getEffectiveDateTime())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(filteredList) && !newStatus.getValue().equals(filteredList.get(filteredList.size() - 1).getValue())) {
                    statusDtoList.add(newStatus);
                    statusDtoList = statusDtoList.stream().sorted(Comparator.comparing(StatusDto::getEffectiveDateTime)).collect(Collectors.toList());
                }
            }
            incomingPFADto.setStatusList(statusDtoList);
        }
        return incomingPFADto;
    }
}
