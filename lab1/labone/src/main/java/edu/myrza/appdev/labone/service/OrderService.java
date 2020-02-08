package edu.myrza.appdev.labone.service;

import edu.myrza.appdev.labone.domain.order.ODish;
import edu.myrza.appdev.labone.domain.order.OIngredient;
import edu.myrza.appdev.labone.domain.order.Order;
import edu.myrza.appdev.labone.domain.order.OUnit;
import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.exception.BadReqException;
import edu.myrza.appdev.labone.payload.dish.FindDishRespBody;
import edu.myrza.appdev.labone.payload.order.ClientReport;
import edu.myrza.appdev.labone.payload.order.CreateReqBody;
import edu.myrza.appdev.labone.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private DishService dishService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        DishService dishService)
    {
        this.orderRepository = orderRepository;
        this.dishService = dishService;
    }

    public String save(CreateReqBody reqBody){

        List<ODish> dishes = reqBody.getDishes().stream()
                                        .map(this::map)
                                        .collect(Collectors.toList());

        Double price = dishes.stream().mapToDouble(ODish::getFullPrice).sum();

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setDishes(dishes);
        order.setPrice(price);

        order = orderRepository.save(order);

        return order.getId();
    }

    public Order getFullReport(String orderId){
        Order order = orderRepository.findById(orderId)
                                        .orElseThrow(noSuchOrder(orderId));

        return order;
    }

    public ClientReport getClientReport(String orderId){

        Order order = orderRepository.findById(orderId)
                                        .orElseThrow(noSuchOrder(orderId));

        ClientReport report = new ClientReport();

        List<ClientReport.DishInfo> dishesInfo = order.getDishes().stream()
                                                        .map(oDish -> {
                                                            ClientReport.DishInfo dishInfo = new ClientReport.DishInfo();
                                                            dishInfo.setId(oDish.getId());
                                                            dishInfo.setName(oDish.getName());
                                                            dishInfo.setPortions(oDish.getPortions());
                                                            dishInfo.setPrice(oDish.getPrice());

                                                            return dishInfo; })
                                                        .collect(Collectors.toList());

        report.setOrderPrice(order.getPrice());
        report.setOrderDate(order.getOrderDate());
        report.setOrderId(order.getId());
        report.setDishes(dishesInfo);

        return report;
    }

    public void deleteReport(String id){
        try {
            orderRepository.deleteById(id);
        }catch (EmptyResultDataAccessException ex){
            //this exception is thrown when we try to delete an entity that doesn't exits
            //do nothing
        }
    }

    private ODish map(CreateReqBody.CrtDishInfo crtDishInfo){

        FindDishRespBody dish = dishService.findById(crtDishInfo.getId());

        ODish data = new ODish();
        data.setName(dish.getName());
        data.setId(dish.getId());
        data.setPrice(dish.getPrice());
        data.setPortions(crtDishInfo.getPortions());


        List<OIngredient> ingredients = dish.getIngredients()
                                                .stream()
                                                .map(this::map)
                                                .collect(Collectors.toList());

        data.setIngredients(ingredients);

        return data;
    }

    private OIngredient map(FindDishRespBody.IngredientData ingData){
        OIngredient OIngredient = new OIngredient();
        OIngredient.setAmount(ingData.getAmount());
        OIngredient.setName(ingData.getIngredient().getName());
        OIngredient.setId(ingData.getIngredient().getId());

        OUnit OUnit = new OUnit();
        OUnit.setName(ingData.getIngredient().getUnit().getName());

        OIngredient.setOUnit(OUnit);

        return OIngredient;
    }

    private static Supplier<BadReqException> noSuchOrder(String orderId){

        BadReqResponseBody body = new BadReqResponseBody.Builder(BadReqCodes.NO_SUCH_ORDER)
                                                        .identifier(orderId)
                                                        .build();

        return () -> new BadReqException(body);
    }

}
