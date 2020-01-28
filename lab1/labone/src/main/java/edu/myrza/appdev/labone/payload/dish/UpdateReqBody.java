package edu.myrza.appdev.labone.payload.dish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UpdateReqBody {

    private String name;
    private Double price;
    private Set<IngredientData> ingredients;

    @Getter
    @Setter
    @NoArgsConstructor
    public class IngredientData {

        //todo whether id is null or not depends on status' value, here we have cross-validation
        private Long id; //ingredient id

        //status values are "NEW","UPD","DEL"
        @NotBlank
        private String status; //todo change to enum later

        @DecimalMin(value = "0.0",inclusive = true)
        private BigDecimal amount;
    }


}
