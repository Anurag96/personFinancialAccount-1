package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class ExtendedPaymentPlanParticipantInfo {

    @ApiModelProperty(value = "code")
    private String code;

    @ApiModelProperty(value = "description")
    private String name;

    @ApiModelProperty(value = "value")
    private String description;
}
