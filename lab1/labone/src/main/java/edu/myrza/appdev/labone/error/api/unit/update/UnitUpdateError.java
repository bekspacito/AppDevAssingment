package edu.myrza.appdev.labone.error.api.unit.update;

import edu.myrza.appdev.labone.payload.unit.UpdateReqBody;

import javax.validation.Payload;

public interface UnitUpdateError extends Payload {
    Object onError(UpdateReqBody reqBody);
}
