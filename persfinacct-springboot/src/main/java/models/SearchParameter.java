package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchParameter implements Serializable {
    private static final long serialVersionUID = 1L;

    public SearchParameter(String customerNumber, String mailGroup, String subGroup){
        this.customerNumber = customerNumber;
        this.mailGroup = mailGroup;
        this.subGroup = subGroup;
    }

    private String individualAGN;

    private String customerNumber;

    private String mailGroup;

    private String subGroup;

    private String membershipId;

    private UUID personResourceId;

}
