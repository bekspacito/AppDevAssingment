package edu.myrza.appdev.labone.payload.dish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class DishUpdReqBody {

    private String name;
    private Double price;
    private Set<DishUpdIngData> ingredients;

}
