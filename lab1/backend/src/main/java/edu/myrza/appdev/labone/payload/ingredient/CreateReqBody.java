package edu.myrza.appdev.labone.payload.ingredient;

import edu.myrza.appdev.labone.error.api.IdNullError;
import edu.myrza.appdev.labone.error.api.NameError;
import edu.myrza.appdev.labone.error.api.PriceError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CreateReqBody {

    @NotBlank(payload = NameError.class)
    private String name;

    @DecimalMin(value = "0.0",payload = PriceError.class)
    private Double price;

    @NotNull(payload = IdNullError.class)
    private Long unitId;

}
