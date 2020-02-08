package edu.myrza.appdev.labone.domain.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ODish {

    private Long id;
    private String name;
    private Double price;
    private Integer portions; //amount of portions
    private List<OIngredient> ingredients;

    public double getFullPrice(){
        return price*portions;
    }

}
