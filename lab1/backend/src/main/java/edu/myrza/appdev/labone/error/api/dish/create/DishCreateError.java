package edu.myrza.appdev.labone.error.api.dish.create;

import edu.myrza.appdev.labone.payload.dish.CreateReqBody;

import javax.validation.Payload;

public interface DishCreateError extends Payload {
    Object onError(CreateReqBody reqBody);
}
