package util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String KEY_COMPONENT_NAME_PATH = "storeKeySet.secondaryKeys.keyComponents.name";
    public static final String KEY_COMPONENT_VALUE_PATH = "storeKeySet.secondaryKeys.keyComponents.value";
    public static final String ACCOUNT_HOLDER_KEY_PATH = "accountHolder.personResourceId";

    public static final String AGN_KEY = "INDIVIDUAL_AGN_ID";
    public static final String CUSTOMER_NUMBER_KEY = "CustomerNumber";
    public static final String MAIL_GROUP_KEY = "MailGroup";
    public static final String SUB_GROUP_KEY = "SubGroup";
    public static final String MEMBERSHIP_ID_KEY = "MembershipId";

    public static final String PERSON_RESOURCE_ID_NOT_VALID = "personResourceId";
    public static final String ACC_HLDR_NOT_VALID = "accountHolder";

    public static final String PFA_SYNC = "pfaSync";
    public static final String PFA_LATEST = "pfaLatest";
    public static final String PFA_CHANGE = "change";

    //Operation Type
    public static final String DELETE = "DELETE";
    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";
    public static final String UNKNOWN = "UNKNOWN";
    public static final String REFRESH = "REFRESH";

    public static final String CMG = "C-M-G";

    public static final String PUBLISHER = "PEPPFACT";

    //TODO check how/what we are getting from the header
    public static final String TENANT_ID = "Tenantid";
    public static final String TRANSACTION_ID = "Transactionid";
}
