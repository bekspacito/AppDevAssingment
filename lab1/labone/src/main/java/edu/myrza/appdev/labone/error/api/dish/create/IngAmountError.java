package edu.myrza.appdev.labone.error.api.dish.create;

import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.FieldErrorCodes;
import edu.myrza.appdev.labone.error.api.dish.update.DishUpdateError;
import edu.myrza.appdev.labone.payload.dish.CreateReqBody;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;

public class IngAmountError implements DishCreateError, DishUpdateError {

    @Override
    public Object onError(CreateReqBody reqBody) {
        return new BadReqResponseBody.Builder(BadReqCodes.FIELD_ERROR)
                                    .fieldError(FieldErrorCodes.INGR_AMOUNT_ERROR)
                                    .build();
    }

    @Override
    public Object onError(UpdateReqBody reqBody) {
        return new BadReqResponseBody.Builder(BadReqCodes.FIELD_ERROR)
                                    .fieldError(FieldErrorCodes.INGR_AMOUNT_ERROR)
                                    .build();
    }
}
