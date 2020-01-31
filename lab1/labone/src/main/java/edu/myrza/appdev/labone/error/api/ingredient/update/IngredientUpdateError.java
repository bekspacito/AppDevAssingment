package edu.myrza.appdev.labone.error.api.ingredient.update;

import edu.myrza.appdev.labone.payload.ingredient.UpdateReqBody;

import javax.validation.Payload;

public interface IngredientUpdateError extends Payload {
    Object onError(UpdateReqBody reqBody);
}
