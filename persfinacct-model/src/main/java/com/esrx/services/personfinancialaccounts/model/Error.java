package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Error
 */
@Data

@AllArgsConstructor
@NoArgsConstructor


public class Error {

    @ApiModelProperty(value = "name")
    private Integer code;

    @ApiModelProperty(value = "name")
    private String message;
}

