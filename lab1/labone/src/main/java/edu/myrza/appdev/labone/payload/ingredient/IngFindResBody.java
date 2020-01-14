package edu.myrza.appdev.labone.payload.ingredient;

import edu.myrza.appdev.labone.payload.unit.UnitRespBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngFindResBody {

    private Long id;
    private String name;
    private Double price;
    private UnitRespBody unit;

}
