package edu.myrza.appdev.labone.util;

import lombok.Getter;

@Getter
public class EntityException extends BadRequestException{

    private Long id;
    private String entityName;

    public EntityException(BadReqSubcodes subcodes,Long id, String entityName){
        super(subcodes);
        this.id = id;
        this.entityName = entityName;
    }
}
