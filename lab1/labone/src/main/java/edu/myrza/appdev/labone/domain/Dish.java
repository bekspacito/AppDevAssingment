package edu.myrza.appdev.labone.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private Double price;

    @OneToMany(mappedBy = "dish",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Set<DishIngredient> dishIngredients = new HashSet<>();

}