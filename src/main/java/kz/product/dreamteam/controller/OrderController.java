package kz.product.dreamteam.controller;

import kz.product.dreamteam.facade.OrderFacade;
import kz.product.dreamteam.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade facade;

    @PostMapping("/add-to-shopping-card")
    public ResponseEntity<OrderDTO> addToShoppingCard(@RequestBody OrderSaveDTO orderSaveDTO) {
        return ResponseEntity.ok(facade.addToShoppingCart(orderSaveDTO));
    }

    @PostMapping("/make-order/{id}")
    public ResponseEntity<OrderDTO> makeOrder(@PathVariable ObjectId id) {
        return ResponseEntity.ok(facade.makeOrder(id));
    }
}
