package edu.myrza.appdev.labone.payload.unit;

import edu.myrza.appdev.labone.error.api.NameError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CreateReqBody {

    @NotBlank(payload = NameError.class)
    private String name;
}
