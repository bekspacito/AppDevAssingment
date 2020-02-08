package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.order.Order;
import edu.myrza.appdev.labone.exception.BadReqException;
import edu.myrza.appdev.labone.payload.order.ClientReport;
import edu.myrza.appdev.labone.payload.order.CreateReqBody;
import edu.myrza.appdev.labone.payload.order.CreateRespBody;
import edu.myrza.appdev.labone.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateReqBody reqBody){

        CreateRespBody resp = null;

        try {
            String orderId = orderService.save(reqBody);
            resp = new CreateRespBody(orderId);

        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ClientReport> prepClientReport(@PathVariable String orderId){

        ClientReport clientReport = orderService.getClientReport(orderId);

        return ResponseEntity.ok(clientReport);

    }

    @GetMapping("/full/{orderId}")
    public ResponseEntity<Order> prepFullReport(@PathVariable String orderId){

        Order order = orderService.getFullReport(orderId);

        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> delete(@PathVariable String orderId){

        orderService.deleteReport(orderId);

        return ResponseEntity.ok().build();
    }

}