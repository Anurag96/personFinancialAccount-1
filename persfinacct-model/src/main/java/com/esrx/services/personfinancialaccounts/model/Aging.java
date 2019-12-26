package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Required;


/**
 * tbd
 */

@Data
@AllArgsConstructor
@NoArgsConstructor


public class Aging {

    @ApiModelProperty(value = "name")
    private String daysAged;

    @ApiModelProperty(value = "name")
    private CurrencyAmount amount;
}
