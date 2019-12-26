package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * tdb
 */

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PersonFinancialAccountOldestCreditDto {

    private CurrencyAmount creditAmount;

    private LocalDateTime creditDate;
}

