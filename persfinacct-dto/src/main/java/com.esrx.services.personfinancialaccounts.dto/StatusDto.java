package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * This is a generic utility struct for capturing a status and effective dates in entity models.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor

public class StatusDto {

    private LocalDateTime effectiveDateTime;

    private String value;
}

