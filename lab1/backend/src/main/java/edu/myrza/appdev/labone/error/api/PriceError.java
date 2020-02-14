package edu.myrza.appdev.labone.error.api;

import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.FieldErrorCodes;
import edu.myrza.appdev.labone.error.api.dish.create.DishCreateError;
import edu.myrza.appdev.labone.error.api.dish.update.DishUpdateError;
import edu.myrza.appdev.labone.error.api.ingredient.create.IngredientCreateError;
import edu.myrza.appdev.labone.error.api.ingredient.update.IngredientUpdateError;
import edu.myrza.appdev.labone.payload.dish.CreateReqBody;
import edu.myrza.appdev.labone.payload.ingredient.UpdateReqBody;

public class PriceError implements
        DishCreateError,
        DishUpdateError,
        IngredientCreateError,
        IngredientUpdateError
{
    @Override
    public Object onError(CreateReqBody reqBody) {
        return priceError(FieldErrorCodes.DISH_PRICE_ERROR);
    }

    @Override
    public Object onError(edu.myrza.appdev.labone.payload.dish.UpdateReqBody reqBody) {
        return priceError(FieldErrorCodes.DISH_PRICE_ERROR);
    }

    @Override
    public Object onError(edu.myrza.appdev.labone.payload.ingredient.CreateReqBody reqBody) {
        return priceError(FieldErrorCodes.INGR_PRICE_ERROR);
    }

    @Override
    public Object onError(UpdateReqBody reqBody) {
        return priceError(FieldErrorCodes.INGR_PRICE_ERROR);
    }

    private static Object priceError(FieldErrorCodes error){
        return new BadReqResponseBody.Builder(BadReqCodes.FIELD_ERROR)
                            .fieldError(error)
                            .build();
    }
}
