package edu.myrza.appdev.labone.payload.dish;

import edu.myrza.appdev.labone.error.api.IdNullError;
import edu.myrza.appdev.labone.error.api.NameError;
import edu.myrza.appdev.labone.error.api.PriceError;
import edu.myrza.appdev.labone.error.api.dish.annotation.IngAmountConstraint;
import edu.myrza.appdev.labone.error.api.dish.annotation.IngIdConstraint;
import edu.myrza.appdev.labone.error.api.dish.annotation.NullOrNotEmpty;
import edu.myrza.appdev.labone.error.api.dish.annotation.StatusConstraint;
import edu.myrza.appdev.labone.error.api.dish.annotation.groupsequence.IngStatusInfo;
import edu.myrza.appdev.labone.error.api.dish.create.IngAmountError;
import edu.myrza.appdev.labone.error.api.dish.update.StatusBlankError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UpdateReqBody {

    @NullOrNotEmpty(payload = NameError.class)
    private String name; //new name for a dish

    private BigDecimal price;

    @Valid
    private Set<IngredientData> ingredients = new HashSet<>();

    public Optional<String> getName(){
        return Optional.ofNullable(name);
    }

    public Optional<@DecimalMin(value = "0.0",payload = PriceError.class) BigDecimal> getPrice(){
        return Optional.ofNullable(price);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @GroupSequence({IngStatusInfo.class, IngredientData.class})
    @StatusConstraint(payload = StatusBlankError.class,groups = IngStatusInfo.class)
    @IngIdConstraint(payload = IdNullError.class)
    @IngAmountConstraint(payload = IngAmountError.class)
    public static class IngredientData {

        private Long id; //ingredient id

        private String status; //todo change to enum later

        private BigDecimal amount;
    }
}
