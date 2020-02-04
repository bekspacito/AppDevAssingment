package edu.myrza.appdev.labone.error;

public enum BadReqCodes {

    UUNC_VIOLATION,     // Unique unit name constraint violation
    NO_SUCH_UNIT,       // No such unit with that id exist
    UINC_VIOLATION,     // Unique ingredient name constraint violation
    NO_SUCH_INGR,       // No such ingredient with that id exist
    UNIT_IS_IN_USE,     // When we try to delete a unit that is used by ingredients

    NO_SUCH_DISH,     // When user wants to manipulate an entity through id that doesn't exist
    DUPLICATE_ENTITIES, // When there is a collection of entities and the collection has duplicate elements
    UNIQUE_ENTITY,      // When an attempt to save an entity which is already present is the system takes place
    ALREADY_ADDED,

    // When something went wrong during a given field's validation.
    // There is a separate subset of error codes dedicated to field errors
    FIELD_ERROR,

    // When there is an attempt to remove all ingredients from a dish during dish data update.Which is wrong
    // It's ING and not ENTITY because the error is really specific and could happen only in one place in the app
    EMPTY_ING_LIST_DISH,
    ING_IS_IN_USE;



    public String toString(){
        return this.name();
    }

    public Integer getCode(){
        switch (this){
            case NO_SUCH_DISH:      return -100;
            case UNIT_IS_IN_USE:    return -101;

            case UUNC_VIOLATION :   return -102;
            case NO_SUCH_UNIT   :   return -103;

            case UINC_VIOLATION :   return -104;
            case NO_SUCH_INGR   :   return -105;

            case FIELD_ERROR    :   return -106;
            case EMPTY_ING_LIST_DISH:   return -107;
            case UNIQUE_ENTITY  :   return -108;
            case DUPLICATE_ENTITIES:return -109;
            case ALREADY_ADDED:     return -110;
            case ING_IS_IN_USE:     return -111;

            default : throw new IllegalArgumentException("NO SUCH BAD REQUEST CODE");
        }
    }

    public static BadReqResponseBody getRespBody(BadReqCodes code){
        return new BadReqResponseBody.Builder(code)
                                      .build();
    }

}
