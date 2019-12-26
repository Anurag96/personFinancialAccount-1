package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CodeDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeDto {

    private String code;

    private String name;

    private String description;
}
