package edu.myrza.appdev.labone.error.api.dish.validator;

import edu.myrza.appdev.labone.error.api.dish.annotation.IngIdConstraint;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IngIdConstraintValidator implements ConstraintValidator<IngIdConstraint, UpdateReqBody.IngredientData> {

    @Override
    public boolean isValid(UpdateReqBody.IngredientData ingredientData, ConstraintValidatorContext constraintValidatorContext) {

        String status = ingredientData.getStatus();

        if(!status.equals("UPD") && !status.equals("NEW")) return true;
        else return ingredientData.getId() != null;

    }
}
