package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * tdb
 */

@Data
@AllArgsConstructor
@NoArgsConstructor


public class PersonFinancialAccountOldestCredit {

    @ApiModelProperty(value = "credit")
    private CurrencyAmount creditAmount;

    @ApiModelProperty(example = "2018-09-11T18:06:35+00:00", value = "This is the date the last credit was issued.")
    private LocalDateTime creditDate;
}

