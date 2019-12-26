package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the maximum amount a person can hold on balance before a payement is required.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PersonFinancialAccountCurrentFloorLimitDto {

    private CurrencyAmount baseLimit;

    private CurrencyAmount highCostLimit;
}

