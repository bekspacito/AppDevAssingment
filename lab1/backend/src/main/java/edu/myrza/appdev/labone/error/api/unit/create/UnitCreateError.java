package edu.myrza.appdev.labone.error.api.unit.create;

import edu.myrza.appdev.labone.payload.unit.CreateReqBody;

import javax.validation.Payload;

public interface UnitCreateError extends Payload {
    Object onError(CreateReqBody reqBody);
}
