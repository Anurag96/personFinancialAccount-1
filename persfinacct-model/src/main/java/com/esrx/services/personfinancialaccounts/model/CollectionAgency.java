package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * CollectionAgency
 */
@Data

@NoArgsConstructor
@AllArgsConstructor
public class CollectionAgency {

    @ApiModelProperty(example = "d16324e4-08c0-4ccd-ac23-7aa2add9702a", value = "A globally-unique identifier used to reference a transaction.", allowEmptyValue = true)
    private UUID relavitevId;

    @ApiModelProperty(value = "collection agency")
    private String name;

    @ApiModelProperty(value = "name")
    private String trackingIdentifier;

    @ApiModelProperty(example = "2018-09-11T18:06:35+00:00", value = "date")
    private LocalDateTime sentToCollectionAgencyDate;

    @ApiModelProperty(value = "name")
    private CurrencyAmount amount;

    @ApiModelProperty(value = "collection status")
    private Code collectionStatus;

}

