package edu.myrza.appdev.labone.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name="_order") //because 'order' is reserved
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double price;

    //order receiving date
    //todo consider adding order finishing date
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY)
    private Set<DishOrder> dishOrder;
}
