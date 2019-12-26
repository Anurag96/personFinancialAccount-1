package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * ExtendedPaymentPlanDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExtendedPaymentPlanDto {

    private UUID relativeId;

    private OrderReference order;

    private LocalDate dueDate;

    private String terms;

    private CurrencyAmount amount;

    private StatusDto status;

    private CurrencyAmount currentBalance;
}

