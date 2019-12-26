package com.esrx.services.personfinancialaccounts.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * This is a generic utility struct for capturing a status and effective dates in entity models.
 */
@ApiModel(description = "This is a generic utility struct for capturing a status and effective dates in entity models.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Status {

    @ApiModelProperty(example = "2018-09-11T18:06:35+00:00", value = "The date and time (optional) at which the item became effective. The format must comply with the ISO 8601 standard. The time component is optional.")
    private LocalDateTime effectiveDateTime;
    @ApiModelProperty(value = "The status. The default allowed values are defined within the context of the entity service and structure being annotated.")
    private String value;

    public enum StatusType {
        ACTIVE("ACTIVE"),
        ON_HOLD("ON-HOLD"),
        DECEASED("DECEASED"),
        INACTIVE("INACTIVE");

        private final String status;

        private StatusType(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}

