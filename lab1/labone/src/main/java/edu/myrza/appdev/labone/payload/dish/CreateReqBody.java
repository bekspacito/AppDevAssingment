package edu.myrza.appdev.labone.payload.dish;

import edu.myrza.appdev.labone.error.api.IdNullError;
import edu.myrza.appdev.labone.error.api.NameError;
import edu.myrza.appdev.labone.error.api.PriceError;
import edu.myrza.appdev.labone.error.api.dish.create.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
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

    @NotBlank(payload = NameError.class)
    private String name;

    @DecimalMin(value = "0.0",inclusive = true,payload = PriceError.class)
    private BigDecimal price;

    @NotNull(payload = IngCollecError.class)
    @Size(min = 1,payload = IngCollecError.class)
    @Valid
    private Set<IngredientData> ingredients;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class IngredientData {

        @NotNull(payload = IdNullError.class)
        private Long id; //ingredient id

        @DecimalMin(value = "0.0",inclusive = true,payload = IngAmountError.class)
        private BigDecimal amount;

    }

}
