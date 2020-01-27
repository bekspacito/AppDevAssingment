package edu.myrza.appdev.labone.error;

public enum FieldErrorCodes {

    IS_NULL,
    BELOW_ZERO,
    IS_EMPTY;

    public Integer getCode(){
        switch (this){
            case IS_NULL: return -1;
            case IS_EMPTY: return -2;
            case BELOW_ZERO: return -3;
            default: throw new IllegalArgumentException("No such field error code...");
        }
    }
}
