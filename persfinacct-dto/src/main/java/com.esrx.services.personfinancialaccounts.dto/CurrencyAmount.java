package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * The Currency Amount is struct used to represent an amount in some currency.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyAmount {

    private BigDecimal amount;

    private String currency;
}

