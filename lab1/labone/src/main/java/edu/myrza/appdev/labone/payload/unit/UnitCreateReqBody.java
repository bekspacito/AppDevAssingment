package edu.myrza.appdev.labone.payload.unit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UnitCreateReqBody {

    @NotBlank
    private String name;
}
