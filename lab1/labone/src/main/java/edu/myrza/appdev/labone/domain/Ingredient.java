package edu.myrza.appdev.labone.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String name;

    private Double price;

    //optional = false means non-NULL relationship must exist
    //you cannot save an ingredient without a unit
    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "unit_id",nullable = false)
    private Unit unit;

    @OneToMany(mappedBy = "ingredient",fetch = FetchType.LAZY)
    private Set<DishIngredient> dishIngredients;

}
