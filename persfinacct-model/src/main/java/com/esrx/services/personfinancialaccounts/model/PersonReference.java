package com.esrx.services.personfinancialaccounts.model;

import com.esrx.services.personfinancialaccounts.util.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;


/**
 * TBD
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonReference {

    @ApiModelProperty(example = "d16324e4-08c0-4ccd-ac23-7aa2add9702a", value = "A globally-unique identifier used to reference a Person entity instance.")
    @NotNull(message = Constants.PERSON_RESOURCE_ID_NOT_VALID)
    private UUID personResourceId;
}

