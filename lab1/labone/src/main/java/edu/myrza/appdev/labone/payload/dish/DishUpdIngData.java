package edu.myrza.appdev.labone.payload.dish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//is used to update dish data
@Getter
@Setter
@NoArgsConstructor
public class DishUpdIngData {
     private Long id; //ingredient id
     private Double amount;
}
