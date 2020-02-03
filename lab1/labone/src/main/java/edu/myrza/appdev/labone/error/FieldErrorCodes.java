package edu.myrza.appdev.labone.error;

public enum FieldErrorCodes {

    DISH_NAME_REQUIRED,
    DISH_PRICE_ERROR,

    INGR_NAME_REQUIRED,
    INGR_NAME_ERROR,   // If 'name' field is present in input data but is empty during update
    NO_INGR_PRESENT,
    INGR_ID_REQUIRED,
    INGR_AMOUNT_ERROR,
    INGR_PRICE_ERROR,
    INGR_STATUS_ERROR,

    UNIT_NAME_REQUIRED,
    UNIT_ID_REQUIRED;

    public Integer getCode(){
        switch (this){
            case DISH_NAME_REQUIRED:        return -100;
            case DISH_PRICE_ERROR:          return -101;

            case INGR_NAME_REQUIRED:        return -102;
            case INGR_NAME_ERROR:           return -103;
            case NO_INGR_PRESENT:           return -104;
            case INGR_ID_REQUIRED:          return -105;
            case INGR_AMOUNT_ERROR:         return -106;
            case INGR_PRICE_ERROR:          return -107;
            case INGR_STATUS_ERROR:         return -108;

            case UNIT_NAME_REQUIRED:        return -109;
            case UNIT_ID_REQUIRED:          return -110;
            default: throw new IllegalArgumentException("No such field error code...");
        }
    }
}
