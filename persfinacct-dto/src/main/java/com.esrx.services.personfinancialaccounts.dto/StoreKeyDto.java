package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The StoreKey struct is a Common Data Struct that holds ESI-specific system identifiers and compound keys comprised of those identifiers. The struct contains two sections, one for primary keys and one for secondary keys; each is of type StoreKey Struct.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor

public class StoreKeyDto {

    private String storeName;

    private String keyName;

    private List<KeyComponentDto> keyComponents;

    private StatusDto status;
}

