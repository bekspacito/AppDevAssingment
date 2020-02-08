package edu.myrza.appdev.labone.payload.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ClientReport {

    private String orderId;
    private Double orderPrice;
    private LocalDateTime orderDate;
    private List<DishInfo> dishes = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DishInfo {

        private Long Id;
        private String name;
        private Double price;
        private Integer portions;

    }

}
