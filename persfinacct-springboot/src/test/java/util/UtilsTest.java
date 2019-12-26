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
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    private Utils utils = new Utils();

    @Test
    public void testGetUUID() throws Exception {
        assertEquals(UUID.class, utils.getUUID().getClass());
    }

    @Test
    public void testGetLocalDateTimeNow() throws Exception {
        LocalDateTime localDateTime = utils.getLocalDateTimeNow();
        int compare = LocalDateTime.now().compareTo(localDateTime);
        assertTrue(compare >= 0);
    }

    @Test(expected = ParametersRequiredException.class)
    public void testGetUUIDFromStringInvalidUUID() {
        UUID personResourceId = utils.getUUIDFromString("k3499bec-9032-4fe2-9441-442662e0b43f", "Person ResourceId");
        System.out.print(personResourceId);
    }

    @Test
    public void testGetUUIDFromStringValidUUID() {
        UUID personResourceId = utils.getUUIDFromString("23499bec-9032-4fe2-9441-442662e0b43f", "Person ResourceId");
        assertEquals("23499bec-9032-4fe2-9441-442662e0b43f", personResourceId.toString());
    }

    @Test
    public void testPrepareStoreKey() {
        StoreKey storeKey = utils.prepareStoreKey("HomeDelivery", "AGN", null, "INDIVIDUAL_AGN_ID", "04566666667");

        StoreKey storeKey1 = new StoreKey();
        storeKey1.setKeyName("AGN");
        storeKey1.setStoreName("HomeDelivery");
        storeKey1.setStatus(null);

        KeyComponent keyComponent = new KeyComponent();
        keyComponent.setName("INDIVIDUAL_AGN_ID");
        keyComponent.setValue("04566666667");
        storeKey1.setKeyComponents(Arrays.asList(keyComponent));

        assertEquals(storeKey, storeKey1);
    }

    @Test
    public void testPrepareStoreKeyWithKeyComponents() {

        StoreKey storeKey1 = new StoreKey();
        storeKey1.setKeyName("AGN");
        storeKey1.setStoreName("HomeDelivery");
        storeKey1.setStatus(null);

        KeyComponent keyComponent = new KeyComponent();
        keyComponent.setName("INDIVIDUAL_AGN_ID");
        keyComponent.setValue("04566666667");
        storeKey1.setKeyComponents(Arrays.asList(keyComponent));

        StoreKey storeKey = utils.prepareStoreKey("HomeDelivery", "AGN", null, storeKey1.getKeyComponents());
        assertEquals(storeKey, storeKey1);
    }

    @Test
    public void testPrepareKeyComponent() {
        List<KeyComponent> keyComponents = utils.prepareKeyComponent("MailGroup", "PD2", null);

        List<KeyComponent> keyComponents1 = new ArrayList<>();
        KeyComponent keyComponent = new KeyComponent();
        keyComponent.setName("MailGroup");
        keyComponent.setValue("PD2");
        keyComponents1.add(keyComponent);

        assertEquals(keyComponents, keyComponents1);
    }

    @Test
    public void testBuildSearchCriteria() {
        SearchParameter searchParameter = new SearchParameter();
        Criteria criteria = utils.buildSearchCriteria(searchParameter);
        String criteriaStr = criteria.getCriteriaObject().toString();

        //verify no default values loaded
        Assert.assertFalse(criteriaStr.contains("accountHolder.personResourceId"));
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=MembershipId"));
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=INDIVIDUAL_AGN_ID"));
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=CustomerNumber"));
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=MailGroup"));
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=SubGroup"));

        //verify no data is modified.. whatever passed will be reflected to the criteria
        UUID uuid = utils.getUUID();
        searchParameter.setPersonResourceId(uuid);
        searchParameter.setMembershipId("M1234");
        searchParameter.setIndividualAGN("U1234");
        searchParameter.setCustomerNumber("CUST_NAME");
        searchParameter.setMailGroup("MAIL_GRP");
        searchParameter.setSubGroup("SUB_GRP");

        criteria = utils.buildSearchCriteria(searchParameter);
        criteriaStr = criteria.getCriteriaObject().toString();

        Assert.assertTrue(criteriaStr.contains("accountHolder.personResourceId=" + uuid));

        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=MembershipId"));
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=M1234"));

        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=INDIVIDUAL_AGN_ID"));
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=U1234"));

        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=CustomerNumber"));
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=CUST_NAME"));

        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=MailGroup"));
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=MAIL_GRP"));

        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=SubGroup"));
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=SUB_GRP"));

        //verify data nullified is reflected in the criteria
        searchParameter.setPersonResourceId(null);
        criteria = utils.buildSearchCriteria(searchParameter);
        criteriaStr = criteria.getCriteriaObject().toString();
        Assert.assertFalse(criteriaStr.contains("accountHolder.personResourceId"));

        searchParameter.setMembershipId(null);
        criteria = utils.buildSearchCriteria(searchParameter);
        criteriaStr = criteria.getCriteriaObject().toString();
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=MembershipId"));
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=1234"));

        searchParameter.setIndividualAGN(null);
        criteria = utils.buildSearchCriteria(searchParameter);
        criteriaStr = criteria.getCriteriaObject().toString();
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=INDIVIDUAL_AGN_ID"));
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=U1234"));

        searchParameter.setCustomerNumber(null);
        criteria = utils.buildSearchCriteria(searchParameter);
        criteriaStr = criteria.getCriteriaObject().toString();
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=CustomerNumber"));
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=CUST_NAME"));

        searchParameter.setMailGroup(null);
        criteria = utils.buildSearchCriteria(searchParameter);
        criteriaStr = criteria.getCriteriaObject().toString();
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=MailGroup"));
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=MAIL_GRP"));

        searchParameter.setSubGroup(null);
        criteria = utils.buildSearchCriteria(searchParameter);
        criteriaStr = criteria.getCriteriaObject().toString();
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=SubGroup"));
        Assert.assertFalse(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=SUB_GRP"));
    }

    @Test
    public void testGetAGNCriteria() {
        String criteriaStr = utils.getAGNCriteria("U1234").getCriteriaObject().toString();
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=INDIVIDUAL_AGN_ID"));
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=U1234"));
    }

    @Test
    public void testGetCMGCriteria() {
        List<Criteria> criteriaList = utils.getCMGCriteria("CUST_NAME", "MAIL_GRP", "SUB_GRP");

        String criteriaStr = criteriaList.get(0).getCriteriaObject().toString();
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=CustomerNumber"));
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=CUST_NAME"));

        String criteriaStr1 = criteriaList.get(1).getCriteriaObject().toString();
        Assert.assertTrue(criteriaStr1.contains("storeKeySet.secondaryKeys.keyComponents.name=MailGroup"));
        Assert.assertTrue(criteriaStr1.contains("storeKeySet.secondaryKeys.keyComponents.value=MAIL_GRP"));

        String criteriaStr2 = criteriaList.get(2).getCriteriaObject().toString();
        Assert.assertTrue(criteriaStr2.contains("storeKeySet.secondaryKeys.keyComponents.name=SubGroup"));
        Assert.assertTrue(criteriaStr2.contains("storeKeySet.secondaryKeys.keyComponents.value=SUB_GRP"));
    }

    @Test
    public void testGetMembershipIdCriteria() {
        String criteriaStr = utils.getMembershipIdCriteria("M1234").getCriteriaObject().toString();
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.name=MembershipId"));
        Assert.assertTrue(criteriaStr.contains("storeKeySet.secondaryKeys.keyComponents.value=M1234"));
    }

    @Test
    public void testGetPersonResourceIdCriteria() {
        UUID uuid = utils.getUUID();
        String criteriaStr = utils.getPersonResourceIdCriteria(uuid).getCriteriaObject().toString();
        Assert.assertTrue(criteriaStr.contains("accountHolder.personResourceId=" + uuid));
    }

    @Test
    public void testPrepareChangeMessage() {
        Header headerDetails = new Header();
        headerDetails.setOperation(OperationType.INSERT.name());

        PersonFinancialAccount pfaBeforeChange = new PersonFinancialAccount();
        pfaBeforeChange.setResourceId(utils.getUUID());
        PersonFinancialAccount pfaAfterChange = new PersonFinancialAccount();
        pfaAfterChange.setResourceId(utils.getUUID());

        Message<ChangePersonFinancialAccount> chngMsg = utils.prepareChangeMessage(headerDetails, pfaBeforeChange, pfaAfterChange);
        assertEquals(chngMsg.getHeader().getOperation(), headerDetails.getOperation());

        assertEquals(chngMsg.getPayload().getBeforeData(), pfaBeforeChange);
        assertEquals(chngMsg.getPayload().getData(), pfaAfterChange);
    }

    @Test
    public void testPrepareLatestMsg() {
        Header headerDetails = new Header();
        headerDetails.setOperation(OperationType.INSERT.name());

        PersonFinancialAccount pfaBeforeChange = new PersonFinancialAccount();
        pfaBeforeChange.setResourceId(utils.getUUID());
        PersonFinancialAccount pfaAfterChange = new PersonFinancialAccount();
        pfaAfterChange.setResourceId(utils.getUUID());

        Message<ChangePersonFinancialAccount> chngMsg = utils.prepareChangeMessage(headerDetails, pfaBeforeChange, pfaAfterChange);

        Message<PersonFinancialAccount> latestMessage = utils.prepareLatestMsg(chngMsg);
        assertEquals(latestMessage.getHeader().getOperation(), headerDetails.getOperation());

        assertEquals(latestMessage.getPayload(), pfaAfterChange);
    }

    @Test
    public void testGetHeaderDetailsFromKafkaMessageHeader() {
        Header header = new Header();
        header.setOperation(OperationType.INSERT.name());
        header.setTransactionId(utils.getUUID().toString());
        header.setOriginalSource("OriginalSource");
        header.setSourceDateTime(utils.getCurrentDateAsString());
        header.setSourceTableName("SourceTableName");
        header.setTenantId(12345678L);
        header.setPublisher(Constants.PUBLISHER);

        Header headerDetails = utils.getHeaderDetailsFromKafkaMessageHeader(header);
        assertEquals(header.getOperation(), headerDetails.getOperation());
        assertEquals(header.getTransactionId(), headerDetails.getTransactionId());
        assertEquals(header.getOriginalSource(), headerDetails.getOriginalSource());
        assertEquals(header.getSourceDateTime(), headerDetails.getSourceDateTime());
        assertEquals(header.getSourceTableName(), headerDetails.getSourceTableName());
        assertEquals(header.getTenantId(), headerDetails.getTenantId());
        assertEquals(header.getPublisher(), headerDetails.getPublisher());

        header.setTransactionId(null);
        headerDetails = utils.getHeaderDetailsFromKafkaMessageHeader(header);
        assertNotNull(headerDetails.getTransactionId());
    }

    @Test
    public void testLoadHeaderDetails() {
        Header headerDetails = new Header();
        PersonFinancialAccountDto personFinancialAccountDto = new PersonFinancialAccountDto();
        utils.loadHeaderDetails(headerDetails, personFinancialAccountDto);
        assertEquals(personFinancialAccountDto.getTenantId(), headerDetails.getTenantId());

        headerDetails.setTenantId(123456789L);
        utils.loadHeaderDetails(headerDetails, personFinancialAccountDto);
        assertEquals(personFinancialAccountDto.getTenantId(), headerDetails.getTenantId());
    }

    @Test
    public void testGetHeaderDetailsFromHTTPRequest() {
        Map<String, String> header = new HashMap<>();
        header.put(Constants.TENANT_ID, "1234567");

        Header headerDetails = utils.getHeaderDetailsFromHTTPRequest(header, OperationType.INSERT);
        assertEquals(headerDetails.getTenantId().toString(), header.get(Constants.TENANT_ID).toString());
        assertEquals(OperationType.INSERT.name(), headerDetails.getOperation());
        assertEquals(Constants.PUBLISHER, headerDetails.getPublisher());
        assertEquals(Constants.PUBLISHER, headerDetails.getOriginalSource());
        assertNotNull(headerDetails.getTransactionId());
    }

    @Test
    public void testMergePFADetailsIfPreviousStatusAlreadyExists() {
        PersonFinancialAccountDto existingPFADto = TestUtils.getPfaDtoMock(LocalDateTime.now());
        PersonFinancialAccountDto incomingPFADto = new PersonFinancialAccountDto();
        incomingPFADto.setStatusList(Arrays.asList(new StatusDto(LocalDateTime.now().minusDays(9), Status.StatusType.ON_HOLD.getStatus())));
        PersonFinancialAccountDto mergedDTO = utils.mergePFADetails(existingPFADto, incomingPFADto);
        assertEquals(existingPFADto.getStatusList().stream().sorted(Comparator.comparing(StatusDto::getEffectiveDateTime)).collect(Collectors.toList()), mergedDTO.getStatusList());
    }

    @Test
    public void testMergePFADetailsIfLatestStatusAlreadyExists() {
        PersonFinancialAccountDto existingPFADto = TestUtils.getPfaDtoMock(LocalDateTime.now());
        PersonFinancialAccountDto incomingPFADto = new PersonFinancialAccountDto();
        incomingPFADto.setStatusList(Arrays.asList(new StatusDto(LocalDateTime.now().plusDays(1), Status.StatusType.ACTIVE.getStatus())));
        PersonFinancialAccountDto mergedDTO = utils.mergePFADetails(existingPFADto, incomingPFADto);
        assertEquals(existingPFADto.getStatusList().stream().sorted(Comparator.comparing(StatusDto::getEffectiveDateTime)).collect(Collectors.toList()), mergedDTO.getStatusList());
    }

    @Test
    public void testMergePFADetailsIfPreviousStatusAlreadyExists1() {
        LocalDateTime currDate = LocalDateTime.now();
        PersonFinancialAccountDto existingPFADto = TestUtils.getPfaDtoMock(currDate);
        PersonFinancialAccountDto incomingPFADto = new PersonFinancialAccountDto();
        incomingPFADto.setStatusList(Arrays.asList(new StatusDto(currDate.minusDays(9), Status.StatusType.INACTIVE.getStatus())));
        PersonFinancialAccountDto mergedDTO = utils.mergePFADetails(existingPFADto, incomingPFADto);
        existingPFADto.getStatusList().add(new StatusDto(currDate.minusDays(9), Status.StatusType.INACTIVE.getStatus()));
        assertEquals(existingPFADto.getStatusList().stream().sorted(Comparator.comparing(StatusDto::getEffectiveDateTime)).collect(Collectors.toList()), mergedDTO.getStatusList());
    }
}
