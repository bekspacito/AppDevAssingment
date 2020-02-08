package edu.myrza.appdev.labone.payload.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateReqBody {

    private Set<DishData> dishes;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DishData{

        private Long id;
        private Integer portions;

    }
}
