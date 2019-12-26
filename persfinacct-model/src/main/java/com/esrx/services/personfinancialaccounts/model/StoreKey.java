package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * The StoreKey struct is a Common Data Struct that holds ESI-specific system identifiers and compound keys comprised of those identifiers. The struct contains two sections, one for primary keys and one for secondary keys; each is of type StoreKey Struct.
 */
@ApiModel(description = " The StoreKey struct is a Common Data Struct that holds ESI-specific system identifiers and compound keys comprised of those identifiers. The struct contains two sections, one for primary keys and one for secondary keys; each is of type StoreKey Struct.")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class StoreKey {

    @ApiModelProperty(required = true, value = "The official name (or acronym) of the store that contains the key components held in the keyComponents array. This should be consistent across entities.")
    private String storeName;

    @ApiModelProperty(value = "A descriptive name of the (possibly composite) key represented by the keyComponents array. This should be chosen to adequately identify the key within code and for discussion purposes.")
    private String keyName;

    @ApiModelProperty(required = true, value = "The key components; each is an identifier in the specified store.")
    private List<KeyComponent> keyComponents;

    @ApiModelProperty(value = "name")
    private Status status;
}

