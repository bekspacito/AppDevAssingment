package edu.myrza.appdev.labone.domain.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private LocalDateTime orderDate;
    private Double price;
    private List<ODish> dishes;


}
