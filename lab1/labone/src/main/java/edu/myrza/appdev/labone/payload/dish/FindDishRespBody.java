package edu.myrza.appdev.labone.payload.dish;

import edu.myrza.appdev.labone.payload.ingredient.FindIngRespBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FindDishRespBody {

    private Long id;
    private String name;
    private Double price;
    private List<FindIngRespBody> ingredients;

}
