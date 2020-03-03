package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.order.Order;
import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.FieldErrorCodes;
import edu.myrza.appdev.labone.exception.BadReqException;
import edu.myrza.appdev.labone.payload.order.ClientReport;
import edu.myrza.appdev.labone.payload.order.CreateReqBody;
import edu.myrza.appdev.labone.payload.order.CreateRespBody;
import edu.myrza.appdev.labone.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validator;
import java.util.List;

@Controller
@RequestMapping("/api/order")
public class OrderController {

    private OrderService orderService;
    private Validator validator;

    @Autowired
    public OrderController(OrderService orderService,
                           Validator validator)
    {
        this.validator = validator;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateReqBody reqBody){

        CreateRespBody resp = null;

        try{
            validator.validate(reqBody)
                    .stream()
                    .findAny()
                    .map(cv -> processError(cv.getMessage(),reqBody))
                    .ifPresent(body -> { throw new BadReqException(body); });

            String orderId = orderService.save(reqBody);
            resp = new CreateRespBody(orderId);

        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> prepClientReport(@PathVariable String orderId){

        ClientReport clientReport = null;

        try {

            clientReport = orderService.getClientReport(orderId);

        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }

        return ResponseEntity.ok(clientReport);

    }

    @GetMapping("/full/{orderId}")
    public ResponseEntity<?> prepFullReport(@PathVariable String orderId){

        Order order = null;

        try {
            order = orderService.getFullReport(orderId);

        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }

        return ResponseEntity.ok(order);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        List<ClientReport> clientReports = orderService.findAll();
        return ResponseEntity.ok(clientReports);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> delete(@PathVariable String orderId){

        orderService.deleteReport(orderId);

        return ResponseEntity.ok().build();
    }

    private BadReqResponseBody processError(String errorName,CreateReqBody reqBody){

        BadReqResponseBody.Builder body = new BadReqResponseBody.Builder(BadReqCodes.FIELD_ERROR);
        body.fieldError(FieldErrorCodes.valueOf(errorName));

        switch (FieldErrorCodes.valueOf(errorName)){
            case DISH_PORTION_ERROR : {
                Long id = reqBody.getDishes()
                                    .stream()
                                    .filter(crtDishInfo -> crtDishInfo.getPortions() == null || crtDishInfo.getPortions() < 0)
                                    .findAny()
                                    .map(CreateReqBody.CrtDishInfo::getId)
                                    .orElse(-1L);

                body.identifier(id);
            }
        }

        return body.build();
    }

}