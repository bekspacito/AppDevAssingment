package edu.myrza.appdev.labone.payload.ingredient;

import edu.myrza.appdev.labone.error.api.IdNullError;
import edu.myrza.appdev.labone.error.api.NameError;
import edu.myrza.appdev.labone.error.api.PriceError;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Optional;

@Setter
@NoArgsConstructor
public class UpdateReqBody {

    private String name;
    private BigDecimal price;
    private Long unitId;

    public Optional<@NotBlank(payload = NameError.class) String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<@DecimalMin(value = "0.0",payload = PriceError.class) BigDecimal> getPrice(){
        return Optional.ofNullable(price);
    }

    public Long getUnitId(){
        return unitId;
    }
}
