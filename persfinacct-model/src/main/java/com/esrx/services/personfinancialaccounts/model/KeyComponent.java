package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The KeyComponent struct is a Common Data Struct that holds a single component of a (possibly) compound StoreKey.  (If there is only one key component for a store key, it is not a compound key.)  This is part of the overall structure of a StoreIdentifiers.
 */
@ApiModel(description = "The KeyComponent struct is a Common Data Struct that holds a single component of a (possibly) compound StoreKey.  (If there is only one key component for a store key, it is not a compound key.)  This is part of the overall structure of a StoreIdentifiers.")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class KeyComponent {

    @ApiModelProperty(required = true, value = "A name that uniquely identifies a specific field in the underlying data store. This does not have to exactly match the specific name of the column in the database (but it can). In general, given the context of a specific entity (e.g., Person, Order, etc), the name can be simplified to not fully qualify it; in that case, the nameInStore attribute would hold the actual fully-qualified name.")
    private String name;

    @ApiModelProperty(required = true, value = "The value of the key identified by the 'name'. This is the value of the column or field in the store that is identified by the 'name' field.")
    private String value;
}

