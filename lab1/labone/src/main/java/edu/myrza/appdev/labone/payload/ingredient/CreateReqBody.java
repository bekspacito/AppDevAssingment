package edu.myrza.appdev.labone.payload.ingredient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class IngCreateReqBody {

    @NotBlank
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Long unitId;

}
