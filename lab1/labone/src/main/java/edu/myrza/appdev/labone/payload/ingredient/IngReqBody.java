package edu.myrza.appdev.labone.payload.ingredient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngReqBody {

    private String name;
    private Double price;
    private Long unitId;

}
