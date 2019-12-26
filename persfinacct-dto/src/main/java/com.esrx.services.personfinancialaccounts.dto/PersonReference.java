package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * TBD
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonReference {

    private UUID personResourceId;
}

