package com.esrx.services.personfinancialaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CollectionAgencyDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionAgencyDto {

    private UUID relavitevId;

    private String name;

    private String trackingIdentifier;

    private LocalDateTime sentToCollectionAgencyDate;

    private CurrencyAmount amount;

    private CodeDto collectionStatus;

}

