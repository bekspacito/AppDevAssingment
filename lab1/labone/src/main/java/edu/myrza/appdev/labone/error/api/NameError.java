package edu.myrza.appdev.labone.error.api;

import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.FieldErrorCodes;
import edu.myrza.appdev.labone.error.api.dish.create.DishCreateError;
import edu.myrza.appdev.labone.error.api.ingredient.create.IngredientCreateError;
import edu.myrza.appdev.labone.error.api.ingredient.update.IngredientUpdateError;
import edu.myrza.appdev.labone.error.api.unit.create.UnitCreateError;
import edu.myrza.appdev.labone.error.api.unit.update.UnitUpdateError;
import edu.myrza.appdev.labone.payload.unit.CreateReqBody;
import edu.myrza.appdev.labone.payload.unit.UpdateReqBody;

public class NameError implements
        UnitCreateError,
        UnitUpdateError,
        IngredientCreateError,
        DishCreateError,
        IngredientUpdateError
{

    @Override
    public Object onError(CreateReqBody reqBody) {
        return nameError(FieldErrorCodes.DISH_NAME_REQUIRED);
    }

    @Override
    public Object onError(UpdateReqBody reqBody) {
        return nameError(FieldErrorCodes.DISH_NAME_REQUIRED);
    }

    @Override
    public Object onError(edu.myrza.appdev.labone.payload.ingredient.CreateReqBody reqBody) {
        return nameError(FieldErrorCodes.INGR_NAME_REQUIRED);
    }

    @Override
    public Object onError(edu.myrza.appdev.labone.payload.dish.CreateReqBody reqBody) {
        return nameError(FieldErrorCodes.DISH_NAME_REQUIRED);
    }

    @Override
    public Object onError(edu.myrza.appdev.labone.payload.ingredient.UpdateReqBody reqBody) {
        return nameError(FieldErrorCodes.INGR_NAME_ERROR);
    }

    private static Object nameError(FieldErrorCodes fieldError){
        return new BadReqResponseBody.Builder(BadReqCodes.FIELD_ERROR)
                                    .fieldError(fieldError)
                                    .build();
    }
}
