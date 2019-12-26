package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The KeyComponentDto struct is a Common Data Struct that holds a single component of a (possibly) compound StoreKey.  (If there is only one key component for a store key, it is not a compound key.)  This is part of the overall structure of a StoreIdentifiers.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyComponentDto {

    private String name;

    private String value;
}

