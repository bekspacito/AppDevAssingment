package edu.myrza.appdev.labone.error;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BadReqResponseBody {

    private Integer code;
    private Map<String,Object> payload;


    private BadReqResponseBody(Builder builder){
        this.code = builder.code.getCode();
        this.payload = builder.payload;
    }

    public static class Builder{
        //required parameter
        private BadReqCodes code;

        //optional parameters
        private Map<String,Object> payload;

        public Builder(BadReqCodes code){
            this.code = code;
            payload = new HashMap<>();
        }

        public Builder entity(String entityName){
            payload.put("entity",entityName);
            return this;
        }

        public Builder identifier(Object identifier){
            payload.put("identifier",identifier);
            return this;
        }

        public Builder fieldError(FieldErrorCodes fieldErrorCode){
            payload.put("fieldError",fieldErrorCode.getCode());
            return this;
        }

        public Builder fieldName(String fieldName){
            payload.put("fieldName",fieldName);
            return this;
        }

        public BadReqResponseBody build(){
            return new BadReqResponseBody(this);
        }

    }

}
