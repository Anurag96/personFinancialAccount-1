package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * This is a representation of a Person Financial Account transaction instance.
 */

@Data
@Document(collection = "personFinancialAccount")
@NoArgsConstructor
@AllArgsConstructor
public class PersonFinancialAccountDto {

    @Id
    @Field("resourceId")
    private UUID resourceId;

    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDateTime;

    private PersonReference accountHolder;

    private CurrencyAmount currentBalance;

    private CurrencyAmount totalChargesInProcess;

    private CurrencyAmount totalPaymentsInProcess;

    private CurrencyAmount totalCreditsInProcess;

    private List<AgingDto> agingBalances;

    private PersonFinancialAccountOldestCreditDto oldestCredit;

    private List<String> collectionLetters;

    private LocalDate lastStatementDate;

    private PersonFinancialAccountCurrentFloorLimitDto currentFloorLimit;

    private CurrencyAmount personalCeilingLimit;

    private LocalDate debtThresholdExceededAsOf;

    private List<StatusDto> creditorEvents;

    private List<CollectionAgencyDto> collectionAgencies;

    private List<ExtendedPaymentPlanDto> extendedPaymentPlans;

    private List<StatusDto> statusList;

    private StoreKeySetDto storeKeySet;

    private Long tenantId;
}
