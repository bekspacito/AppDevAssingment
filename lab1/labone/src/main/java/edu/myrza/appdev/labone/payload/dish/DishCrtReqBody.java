package edu.myrza.appdev.labone.payload.dish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

//todo set validation annotations

@Getter
@Setter
@NoArgsConstructor
public class DishCrtReqBody {

    private String name;
    private Double price;
    private Set<DishCrtIngData> ingredients;

}
