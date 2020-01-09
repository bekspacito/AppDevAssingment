package edu.myrza.appdev.labone.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DishOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "dish_id",nullable = false)
    private Dish dish;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    private Integer portions;
}
