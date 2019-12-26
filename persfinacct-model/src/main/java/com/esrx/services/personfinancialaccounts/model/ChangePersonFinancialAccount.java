package com.esrx.services.personfinancialaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePersonFinancialAccount {
    private PersonFinancialAccount beforeData;
    private PersonFinancialAccount data;
}
