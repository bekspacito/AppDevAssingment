package edu.myrza.appdev.labone.error.api;

import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.FieldErrorCodes;
import edu.myrza.appdev.labone.error.api.dish.create.DishCreateError;
import edu.myrza.appdev.labone.error.api.ingredient.create.IngredientCreateError;
import edu.myrza.appdev.labone.payload.dish.CreateReqBody;

public class IdNullError implements
        DishCreateError,
        IngredientCreateError
{
    @Override
    public Object onError(CreateReqBody reqBody) {
        return idNullError(FieldErrorCodes.INGR_ID_REQUIRED);
    }

    @Override
    public Object onError(edu.myrza.appdev.labone.payload.ingredient.CreateReqBody reqBody) {
        return idNullError(FieldErrorCodes.UNIT_ID_REQUIRED);
    }

    private static Object idNullError(FieldErrorCodes error){
        return new BadReqResponseBody.Builder(BadReqCodes.FIELD_ERROR)
                                        .fieldError(error)
                                        .build();
    }
}
