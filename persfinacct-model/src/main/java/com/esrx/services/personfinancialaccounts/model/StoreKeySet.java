package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * The StoreKeySet struct is a Common Data Struct that holds ESI-specific system identifiers and compound keys comprised of those identifiers. The struct contains two sections, one for primary keys and one for secondary keys; each is of type StoreKey Struct.
 */
@ApiModel(description = "The StoreKeySet struct is a Common Data Struct that holds ESI-specific system identifiers and compound keys comprised of those identifiers. The struct contains two sections, one for primary keys and one for secondary keys; each is of type StoreKey Struct.")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class StoreKeySet {

    @ApiModelProperty(required = false, value = "name")
    private StoreKey primaryKey;

    @ApiModelProperty(value = "The secondary store keys. Cannot be created unless primaryKey is set.")
    private List<StoreKey> secondaryKeys;
}

