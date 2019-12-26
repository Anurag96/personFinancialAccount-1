package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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


public class OrderReference {

    @ApiModelProperty(example = "d16324e4-08c0-4ccd-ac23-7aa2add9702a", value = "A globally-unique identifier used to reference a Order entity instance.")
    private UUID orderResourceId;
}

