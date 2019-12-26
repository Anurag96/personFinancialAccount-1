package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * tbd
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgingDto {

    private String daysAged;

    private CurrencyAmount amount;
}
