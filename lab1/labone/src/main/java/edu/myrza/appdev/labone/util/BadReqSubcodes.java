package edu.myrza.appdev.labone.util;

import edu.myrza.appdev.labone.payload.BadRequestRespBody;

public enum BadReqSubcodes {

    PRICE_BELOW_ZERO,
    NO_SUCH_ENTITY,  // When user wants to manipulate an entity through id that doesn't exist
    WRONG_INPUT,     // When client's input data(usually it's json data) didn't make it through validation

    UUNC_VIOLATION,  // Unique unit name constraint violation
    NO_SUCH_UNIT,    // No such unit with that id exist

    UINC_VIOLATION,  // Unique ingredient name constraint violation
    NO_SUCH_INGR,    // No such ingredient with that id exist

    EMPTY_INGR_NAME, // Ingredient cannot be created without name set
    EMPTY_INGR_PRICE,// Ingredient cannot be created without price set
    EMPTY_INGR_UNIT; // Ingredient cannot be created without unit set

    public String toString(){
        return this.name();
    }

    public Integer getCode(){
        switch (this){
            case NO_SUCH_ENTITY :   return  -99;
            case WRONG_INPUT    :   return -100;

            case UUNC_VIOLATION :   return -101;
            case NO_SUCH_UNIT   :   return -102;

            case UINC_VIOLATION :   return -103;
            case NO_SUCH_INGR   :   return -104;

            case EMPTY_INGR_NAME:   return -105;
            case EMPTY_INGR_UNIT:   return -106;
            case EMPTY_INGR_PRICE:  return -107;

            default : throw new IllegalArgumentException("NO SUCH BAD REQUEST SUBCODE");
        }
    }

    public static BadRequestRespBody getRespBody(BadReqSubcodes subcode){
        BadRequestRespBody respBody = new BadRequestRespBody();
        respBody.setCode(subcode.getCode());

        return respBody;
    }

}
