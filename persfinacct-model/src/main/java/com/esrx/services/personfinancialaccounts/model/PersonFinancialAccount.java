package com.esrx.services.personfinancialaccounts.model;

import com.esrx.services.personfinancialaccounts.util.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * This is a representation of a Person Financial Account transaction instance.
 */
@ApiModel(description = "This is a representation of a Person Financial Account transaction instance.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonFinancialAccount extends ResourceSupport implements Serializable {

    @ApiModelProperty(example = "d16324e4-08c0-4ccd-ac23-7aa2add9702a", value = "A globally-unique identifier used to reference a Person Financial Account transaction.", allowEmptyValue = true)
    private UUID resourceId;

    @ApiModelProperty(example = "2018-09-11T18:06:35+00:00", value = "This is the date received from the consumer")
    private LocalDateTime createdDateTime;

    @ApiModelProperty(example = "2018-09-11T18:06:35+00:00", value = "This is the date received from the consumer")
    private LocalDateTime updatedDateTime;

    @ApiModelProperty(value = "account holder")
    @NotNull(message = Constants.ACC_HLDR_NOT_VALID)
    @Valid
    private PersonReference accountHolder;

    @ApiModelProperty(value = "balance")
    private CurrencyAmount currentBalance;

    @ApiModelProperty(value = "name")
    private CurrencyAmount totalChargesInProcess;

    @ApiModelProperty(value = "amount")
    private CurrencyAmount totalPaymentsInProcess;

    @ApiModelProperty(value = "name")
    private CurrencyAmount totalCreditsInProcess;

    @ApiModelProperty(value = "description")
    private List<Aging> agingBalances;

    @ApiModelProperty(value = "credit")
    private PersonFinancialAccountOldestCredit oldestCredit;

    @ApiModelProperty(value = "collection")
    private List<String> collectionLetters;

    @ApiModelProperty(example = "2018-09-11", value = "This is the date the last time a Statement was issued.")
    private LocalDate lastStatementDate;

    @ApiModelProperty(value = "limit")
    private PersonFinancialAccountCurrentFloorLimit currentFloorLimit;

    @ApiModelProperty(value = "The maximum amount a person can be charged before we need to obtain additional authorization from the account holder.")
    private CurrencyAmount personalCeilingLimit;

    @ApiModelProperty(example = "2018-09-11", value = "This is the date on which the debt threshold was exceeded.")
    private LocalDate debtThresholdExceededAsOf;

    @ApiModelProperty(value = "Allowed Value - BANKRUPT - BOUNCED_CHECKED - WRITE-OFF")
    private List<Status> creditorEvents;

    @ApiModelProperty(value = "collection")
    private List<CollectionAgency> collectionAgencies;

    @ApiModelProperty(value = "Gives information on the member participation of Extended Payment plan")
    private ExtendedPaymentPlanParticipantInfo extendedPaymentPlanParticipant;

    @ApiModelProperty(value = "Allowed Values    - ACTIVE   - ON-HOLD   - DECEASED   - INACTIVE")
    private Status status;

    @ApiModelProperty(value = "name")
    private StoreKeySet storeKeySet;

}
