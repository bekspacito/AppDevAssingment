package edu.myrza.appdev.labone.error.api.dish.validator;

import edu.myrza.appdev.labone.error.api.dish.annotation.StatusConstraint;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class StatusConstraintValidator implements ConstraintValidator<StatusConstraint, UpdateReqBody.IngredientData> {

    private static final List<String> allowedValues = Arrays.asList("NEW","UPD","DEL");

    private static boolean isAllowed(String value){
        return allowedValues.stream().anyMatch(av -> av.equals(value));
    }

    @Override
    public boolean isValid(UpdateReqBody.IngredientData ingredientData, ConstraintValidatorContext constraintValidatorContext) {
        String status = ingredientData.getStatus();
        return status != null && isAllowed(status);
    }

}
