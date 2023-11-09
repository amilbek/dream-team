package kz.product.dreamteam.controller;

import kz.product.dreamteam.facade.OrderFacade;
import kz.product.dreamteam.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade facade;

    @PostMapping("/make-order")
    public ResponseEntity<OrderDTO> updateUserAccount(@RequestBody OrderSaveDTO orderSaveDTO) {
        return ResponseEntity.ok(facade.makeOrder(orderSaveDTO));
    }
}
