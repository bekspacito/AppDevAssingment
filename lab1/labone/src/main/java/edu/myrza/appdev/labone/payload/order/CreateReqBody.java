package edu.myrza.appdev.labone.payload.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateReqBody {

    @NotNull(message = "ORDER_EMPTY_DISH_LIST")
    @Size(min = 1,message = "ORDER_EMPTY_DISH_LIST")
    @Valid
    private Set<CrtDishInfo> dishes;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CrtDishInfo {

        @NotNull(message = "DISH_ID_REQUIRED")
        private Long id;

        @NotNull(message = "DISH_PORTION_ERROR")
        @Min(value = 1,message = "DISH_PORTION_ERROR")
        private Integer portions;

    }
}
