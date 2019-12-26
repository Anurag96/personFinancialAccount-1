package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;


/**
 * ExtendedPaymentPlan
 */
@Data

@AllArgsConstructor
@NoArgsConstructor


public class ExtendedPaymentPlan {

    @ApiModelProperty(example = "d16324e4-08c0-4ccd-ac23-7aa2add9702a", value = "A globally-unique identifier used to reference a Order entity instance.")
    private UUID relativeId;

    @ApiModelProperty(value = "order name")
    @Valid
    private OrderReference order;

    @ApiModelProperty(example = "2018-09-11", value = "Which due date is this? ")
    private LocalDate dueDate;

    @ApiModelProperty(value = "TBD we need to know the payment terms ?  ")
    private String terms;

    @ApiModelProperty(value = "currency")
    private CurrencyAmount amount;

    @ApiModelProperty(value = "description")
    private Status status;

    @ApiModelProperty(value = "amount")
    private CurrencyAmount currentBalance;
}

