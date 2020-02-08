package edu.myrza.appdev.labone.error;

public enum FieldErrorCodes {

    DISH_NAME_REQUIRED, //when we try to create dish without name
    DISH_NAME_ERROR,    //when we try to update dish with name as empty string
    DISH_PRICE_ERROR,
    DISH_ID_REQUIRED,
    DISH_PORTION_ERROR,

    INGR_NAME_REQUIRED,
    INGR_NAME_ERROR,   // If 'name' field is present in input data but is empty during update
    NO_INGR_PRESENT,
    INGR_ID_REQUIRED,
    INGR_AMOUNT_ERROR,
    INGR_PRICE_ERROR,
    INGR_STATUS_ERROR,

    UNIT_NAME_REQUIRED,
    UNIT_ID_REQUIRED,

    ORDER_EMPTY_DISH_LIST;

    //todo move error codes into separate file and pull them when needed
    public static Integer getCode(String code){
        for(FieldErrorCodes _code : FieldErrorCodes.values()){
            if(_code.name().equals(code)) return _code.getCode();
        }

        return -1;
    }

    public Integer getCode(){
        switch (this){
            case DISH_NAME_REQUIRED:        return -100;
            case DISH_NAME_ERROR:           return -101;
            case DISH_PRICE_ERROR:          return -102;
            case DISH_ID_REQUIRED:          return -103;
            case DISH_PORTION_ERROR:        return -104;

            case INGR_NAME_REQUIRED:        return -105;
            case INGR_NAME_ERROR:           return -106;
            case NO_INGR_PRESENT:           return -107;
            case INGR_ID_REQUIRED:          return -108;
            case INGR_AMOUNT_ERROR:         return -109;
            case INGR_PRICE_ERROR:          return -110;
            case INGR_STATUS_ERROR:         return -111;

            case UNIT_NAME_REQUIRED:        return -112;
            case UNIT_ID_REQUIRED:          return -113;

            case ORDER_EMPTY_DISH_LIST:     return -114;
            default: throw new IllegalArgumentException("No such field error code...");
        }
    }
}
