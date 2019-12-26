package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This is the maximum amount a person can hold on balance before a payement is required.
 */
@ApiModel(description = "This is the maximum amount a person can hold on balance before a payement is required.")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class PersonFinancialAccountCurrentFloorLimit {

    @ApiModelProperty(value = "limit")
    private CurrencyAmount baseLimit;

    @ApiModelProperty(value = "currency")
    private CurrencyAmount highCostLimit;
}

