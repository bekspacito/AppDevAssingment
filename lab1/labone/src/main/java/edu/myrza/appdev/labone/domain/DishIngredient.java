package edu.myrza.appdev.labone.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(
    uniqueConstraints =
        @UniqueConstraint(columnNames={"ingredient_id","dish_id"}) //make combination unique
)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class DishIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "ingredient_id",nullable = false)
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "dish_id",nullable = false)
    private Dish dish;

    private Double amount;
    private String description;

}
