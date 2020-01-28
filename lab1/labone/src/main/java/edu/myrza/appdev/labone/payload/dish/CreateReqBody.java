package edu.myrza.appdev.labone.payload.dish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

//todo set validation annotations

@Getter
@Setter
@NoArgsConstructor
public class CreateReqBody {

    @NotBlank
    private String name;

    @DecimalMin(value = "0.0",inclusive = true)
    private BigDecimal price;

    @NotNull
    @Size
    private Set<IngredientData> ingredients;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class IngredientData {

        @NotNull
        private Long id; //ingredient id

        @DecimalMin(value = "0.0",inclusive = true)
        private BigDecimal amount;

        //todo implement equals and hashcode
    }

}
