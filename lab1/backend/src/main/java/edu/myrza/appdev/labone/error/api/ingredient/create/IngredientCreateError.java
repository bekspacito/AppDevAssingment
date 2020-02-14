package edu.myrza.appdev.labone.error.api.ingredient.create;

import edu.myrza.appdev.labone.payload.ingredient.CreateReqBody;

import javax.validation.Payload;

public interface IngredientCreateError extends Payload {
    Object onError(CreateReqBody reqBody);
}
