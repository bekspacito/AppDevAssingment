package edu.myrza.appdev.labone.error.api.dish.validator;

import edu.myrza.appdev.labone.error.api.dish.annotation.IngAmountConstraint;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class IngAmountConstraintValidator implements ConstraintValidator<IngAmountConstraint, UpdateReqBody.IngredientData> {

    @Override
    public boolean isValid(UpdateReqBody.IngredientData ingredientData, ConstraintValidatorContext constraintValidatorContext) {

        String status = ingredientData.getStatus();

        if(!status.equals("UPD") && !status.equals("NEW")) return true;
        else return ingredientData.getAmount() != null &&
                    ingredientData.getAmount().compareTo(BigDecimal.ZERO) >= 0;

    }
}
