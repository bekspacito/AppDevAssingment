package edu.myrza.appdev.labone.error.api.dish.update;

import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;

import javax.validation.Payload;

public interface DishUpdateError extends Payload {
    Object onError(UpdateReqBody reqBody);
}
