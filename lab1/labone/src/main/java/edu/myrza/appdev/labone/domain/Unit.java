package edu.myrza.appdev.labone.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "UNIT_UNIQUE_NAME",columnNames = {"name"})
)
@Getter
@Setter
@NoArgsConstructor
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "unit",fetch = FetchType.LAZY)
    private Set<Ingredient> ingredients;

}
