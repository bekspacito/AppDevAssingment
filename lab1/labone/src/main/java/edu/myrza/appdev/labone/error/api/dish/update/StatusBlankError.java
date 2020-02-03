package edu.myrza.appdev.labone.error.api.dish.update;

import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.FieldErrorCodes;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;

public class StatusBlankError implements DishUpdateError{
    @Override
    public Object onError(UpdateReqBody reqBody) {
        return new BadReqResponseBody.Builder(BadReqCodes.FIELD_ERROR)
                                    .fieldError(FieldErrorCodes.INGR_STATUS_ERROR)
                                    .build();
    }
}
