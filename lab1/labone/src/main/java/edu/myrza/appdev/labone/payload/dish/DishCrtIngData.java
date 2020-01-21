package edu.myrza.appdev.labone.payload.dish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//todo set validation annotations

//is used to update dish data
@Getter
@Setter
@NoArgsConstructor
public class DishCrtIngData {
    private Long id; //ingredient id
    private Double amount;
}
