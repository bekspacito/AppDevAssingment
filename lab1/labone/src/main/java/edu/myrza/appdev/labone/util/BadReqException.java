package edu.myrza.appdev.labone.util;

import edu.myrza.appdev.labone.payload.BadRequestRespBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadReqException extends RuntimeException{

    private BadRequestRespBody respBody;

    public BadReqException(Object id,Integer code,String entityName){
        respBody = new BadRequestRespBody();
        respBody.setIdentifier(id);
        respBody.setCode(code);
        respBody.setEntityName(entityName);
    }

}
