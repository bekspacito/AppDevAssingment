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
     //status values are "NEW","UPD","DEL"
     private String status; //todo change to enum later
     private Double amount;
}
