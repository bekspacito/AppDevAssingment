package edu.myrza.appdev.labone.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "ING_UNIQUE_NAME",columnNames = {"name"})
)
@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    private Double price;

    //optional = false means non-NULL relationship must exist
    //you cannot save an ingredient without a unit
    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "unit_id",foreignKey = @ForeignKey(name="FK_ING_UNIT"),nullable = false)
    private Unit unit;

    @OneToMany(mappedBy = "ingredient",fetch = FetchType.LAZY)
    private Set<DishIngredient> dishIngredients;

    public void setBDPrice(BigDecimal price){
        this.price = price.doubleValue();
    }

}
