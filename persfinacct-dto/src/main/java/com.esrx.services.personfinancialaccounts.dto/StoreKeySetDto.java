package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The StoreKeySet struct is a Common Data Struct that holds ESI-specific system identifiers and compound keys comprised of those identifiers. The struct contains two sections, one for primary keys and one for secondary keys; each is of type StoreKey Struct.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreKeySetDto {

    private StoreKeyDto primaryKey;

    private List<StoreKeyDto> secondaryKeys;
}

