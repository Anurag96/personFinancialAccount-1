package util;

import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.dto.StatusDto;
import com.esrx.services.personfinancialaccounts.model.ExtendedPaymentPlan;
import com.esrx.services.personfinancialaccounts.model.ExtendedPaymentPlanParticipantInfo;
import com.esrx.services.personfinancialaccounts.model.KeyComponent;
import com.esrx.services.personfinancialaccounts.model.OrderReference;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.PersonReference;
import com.esrx.services.personfinancialaccounts.model.Status;
import com.esrx.services.personfinancialaccounts.model.StoreKey;
import com.esrx.services.personfinancialaccounts.model.StoreKeySet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@UtilityClass
public class TestUtils {

    private Utils utils = new Utils();

    public PersonFinancialAccount createFinAccObject() {
        PersonFinancialAccount finacc = new PersonFinancialAccount();
        finacc.setResourceId(UUID.randomUUID());

        PersonReference accountHolder = new PersonReference();
        accountHolder.setPersonResourceId(utils.getUUID());
        finacc.setAccountHolder(accountHolder);
        finacc.setResourceId(utils.getUUID());

        PersonReference personReference = new PersonReference();
        personReference.setPersonResourceId(UUID.randomUUID());
        finacc.setAccountHolder(personReference);

        ExtendedPaymentPlanParticipantInfo extendedPaymentPlanParticipant = new ExtendedPaymentPlanParticipantInfo();
        ExtendedPaymentPlan extendedPaymentPlan = new ExtendedPaymentPlan();
        extendedPaymentPlan.setRelativeId(UUID.randomUUID());

        OrderReference orderReference = new OrderReference();
        orderReference.setOrderResourceId(UUID.randomUUID());
        extendedPaymentPlan.setOrder(orderReference);

        finacc.setExtendedPaymentPlanParticipant(extendedPaymentPlanParticipant);

        StoreKeySet storeKeySet = new StoreKeySet();
        List<StoreKey> secondayKeys = new ArrayList<>();
        secondayKeys.add(utils.prepareStoreKey("HomeDelivery", "AGN", null, "INDIVIDUAL_AGN_ID", "04566666667"));
        secondayKeys.add(utils.prepareStoreKey("HomeDelivery", "M", null, "MembershipId", "045669234345"));

        StoreKey cmgStoreKey = utils.prepareStoreKey("HomeDelivery", "C-M-G", null, "CustomerNumber", "U344444449");
        List<KeyComponent> keyComponents = cmgStoreKey.getKeyComponents();
        utils.prepareKeyComponent("MailGroup", "PD2", keyComponents);
        utils.prepareKeyComponent("SubGroup", "KHXB", keyComponents);
        storeKeySet.setSecondaryKeys(secondayKeys);
        finacc.setStoreKeySet(storeKeySet);

        return finacc;
    }

    public String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getItemAsString(PersonFinancialAccount personFinancialAccount) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(personFinancialAccount);
    }

    public PersonFinancialAccountDto getPfaDtoMock(LocalDateTime currentDateTime) {
        PersonFinancialAccountDto personFinancialAccountDto = new PersonFinancialAccountDto();
        List<StatusDto> statusDtos = new ArrayList<>();
        statusDtos.add(new StatusDto(currentDateTime.minusDays(10), Status.StatusType.ON_HOLD.getStatus()));
        statusDtos.add(new StatusDto(currentDateTime.plusDays(10), Status.StatusType.DECEASED.getStatus()));
        statusDtos.add(new StatusDto(currentDateTime, Status.StatusType.ACTIVE.getStatus()));
        personFinancialAccountDto.setStatusList(statusDtos);
        return personFinancialAccountDto;
    }

    public PersonFinancialAccount addCMG(PersonFinancialAccount personFinancialAccount) {

        if (null != personFinancialAccount) {
            StoreKey storeKey = new StoreKey();
            List<KeyComponent> keyComponents = new ArrayList<>();

            storeKey.setKeyName("C-M-G");

            KeyComponent keyComponent1 = new KeyComponent();
            keyComponent1.setValue("710556462NPY");
            keyComponent1.setName("CustomerNumber");

            KeyComponent keyComponent2 = new KeyComponent();
            keyComponent2.setValue("BDD");
            keyComponent2.setName("MailGroup");

            KeyComponent keyComponent3 = new KeyComponent();
            keyComponent3.setValue("1192BBD");
            keyComponent3.setName("SubGroup");

            keyComponents.add(keyComponent1);
            keyComponents.add(keyComponent2);
            keyComponents.add(keyComponent3);

            storeKey.setKeyComponents(keyComponents);

            StoreKeySet storeKeySet = new StoreKeySet();
            List<StoreKey> secondaryKeys = new ArrayList<>();
            secondaryKeys.add(storeKey);

            storeKeySet.setSecondaryKeys(secondaryKeys);

            personFinancialAccount.setStoreKeySet(storeKeySet);

            return personFinancialAccount;
        } else {
            return new PersonFinancialAccount();
        }
    }
}
