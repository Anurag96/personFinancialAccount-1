package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Code
 */
@Data

@AllArgsConstructor
@NoArgsConstructor


public class Code {

    @ApiModelProperty(required = false, value = "code name")
    private String code;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "description")
    private String description;
}

