package edu.myrza.appdev.labone.payload.dish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


//todo set validation annotations

//is used to update dish data
@Getter
@Setter
@NoArgsConstructor
public class DishCrtIngData {

    @NotNull(message = "Ingredient id cannot be null")
    private Long id; //ingredient id

    @DecimalMin(value = "0.0",inclusive = true,message = "amount must be above or equal to zero")
    private BigDecimal amount;

}
