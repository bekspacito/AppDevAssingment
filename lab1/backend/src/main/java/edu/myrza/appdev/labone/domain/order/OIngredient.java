package edu.myrza.appdev.labone.domain.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OIngredient {

    private Long id;
    private String name;
    //amount of given ingredient in this dish
    private Double amount;
    private OUnit OUnit;

}
