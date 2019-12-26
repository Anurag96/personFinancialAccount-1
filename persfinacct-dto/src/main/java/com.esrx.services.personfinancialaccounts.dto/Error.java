package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Error {

    private Integer code;

    private String message;
}

