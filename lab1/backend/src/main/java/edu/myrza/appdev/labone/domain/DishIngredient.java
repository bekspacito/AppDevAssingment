package edu.myrza.appdev.labone.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(name = "ING_DISH_UNIQUE",columnNames={"ingredient_id","dish_id"}) //make combination unique
)
@Getter
@Setter
@NoArgsConstructor
public class DishIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "ingredient_id",foreignKey = @ForeignKey(name = "FK_DISH_ING_ING"),nullable = false)
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "dish_id",nullable = false)
    private Dish dish;

    private Double amount;
    private String description;

}
