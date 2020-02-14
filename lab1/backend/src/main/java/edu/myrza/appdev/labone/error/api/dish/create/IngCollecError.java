package edu.myrza.appdev.labone.error.api.dish.create;

import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.FieldErrorCodes;
import edu.myrza.appdev.labone.payload.dish.CreateReqBody;

public class IngCollecError implements DishCreateError{
    @Override
    public Object onError(CreateReqBody reqBody) {
        return new BadReqResponseBody.Builder(BadReqCodes.FIELD_ERROR)
                                    .fieldError(FieldErrorCodes.NO_INGR_PRESENT)
                                    .build();
    }
}
