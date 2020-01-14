package edu.myrza.appdev.labone.util;

import edu.myrza.appdev.labone.payload.BadRequestRespBody;

public enum BadReqSubcodes {

    UUNC_VIOLATION, // Unique unit name constraint violation
    NO_SUCH_UNIT,   // No such unit with that id exist

    UINC_VIOLATION, // Unique ingredient name constraint violation
    NO_SUCH_INGR;   // No such ingredient with that id exist

    public Integer getCode(){
        switch (this){
            case UUNC_VIOLATION :   return 100;
            case NO_SUCH_UNIT   :   return 101;

            case UINC_VIOLATION :   return 102;
            case NO_SUCH_INGR   :   return 103;

            default : throw new IllegalArgumentException("NO SUCH BAD REQUEST SUBCODE");
        }
    }

    public static BadRequestRespBody getRespBody(BadReqSubcodes subcode){
        BadRequestRespBody respBody = new BadRequestRespBody();
        respBody.setCode(subcode.getCode());

        return respBody;
    }

}
