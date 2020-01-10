package edu.myrza.appdev.labone.util;

public enum BadReqSubcodes {

    UUNC_VIOLATION, // Unique unit name constraint violation
    NO_SUCH_UNIT;   // No such unit with that id

    public Integer getCode(){
        switch (this){
            case UUNC_VIOLATION : return 100;
            case NO_SUCH_UNIT   : return 101;
            default : throw new IllegalArgumentException("NO SUCH BAD REQUEST SUBCODE");
        }
    }

}
