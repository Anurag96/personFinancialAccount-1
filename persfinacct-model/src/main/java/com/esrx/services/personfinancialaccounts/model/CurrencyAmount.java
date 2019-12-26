package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * The Currency Amount is struct used to represent an amount in some currency.
 */
@ApiModel(description = "The Currency Amount is struct used to represent an amount in some currency. ")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyAmount {

    @ApiModelProperty(value = "The amount. This can be a whole number or include fractional units in the currency specified by the currency attribute.")
    private BigDecimal amount;

    @ApiModelProperty(value = "The currency the amount is in. If not specified, default is USD (US dollars). Generally, this will not be specified, as most of ESI's business is in terms of US dollars.")
    private String currency;
}

