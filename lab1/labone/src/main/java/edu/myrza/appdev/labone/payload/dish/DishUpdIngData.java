package edu.myrza.appdev.labone.payload.dish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

//is used to update dish data
@Getter
@Setter
@NoArgsConstructor
public class DishUpdIngData {

     //todo whether id is null or not depends on status' value, here we have cross-validation

     private Long id; //ingredient id
     //status values are "NEW","UPD","DEL"
     @NotBlank(message = "Status cannot be blank")
     private String status; //todo change to enum later

     @DecimalMin(value = "0.0",inclusive = true,message = "amount must be above or equal to zero")
     private BigDecimal amount;
}
