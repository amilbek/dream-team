package kz.product.dreamteam.controller;

import kz.product.dreamteam.facade.OrderFacade;
import kz.product.dreamteam.model.dto.*;
import kz.product.dreamteam.model.dto.request.FilterRequest;
import kz.product.dreamteam.model.dto.request.SortRequest;
import kz.product.dreamteam.model.dto.request.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade facade;


    @PostMapping("/add-to-shopping-cart")
    public ResponseEntity<OrderDTO> addToShoppingCart(@RequestBody OrderPositionSaveDTO orderPositionSaveDTO) {
        return ResponseEntity.ok(facade.addToShoppingCart(orderPositionSaveDTO));
    }


    @PostMapping("/remove-from-shopping-cart")
    public ResponseEntity<OrderDTO> removeFromShoppingCart(@RequestBody OrderPositionSaveDTO orderPositionSaveDTO) {
        return ResponseEntity.ok(facade.removeFromShoppingCart(orderPositionSaveDTO));
    }

    @PostMapping("/make-order/{id}")
    public ResponseEntity<OrderDTO> makeOrder(@PathVariable ObjectId id) {
        return ResponseEntity.ok(facade.makeOrder(id));
    }

    @PostMapping("/search")
    public ResponseEntity<Collection<OrderDTO>> search(@RequestBody SearchRequest<FilterRequest, SortRequest> request) {
        return ResponseEntity.ok(facade.search(request));
    }

    @GetMapping("/my-shopping-cart")
    public ResponseEntity<OrderDTO> getMyShoppingCart() {
        return ResponseEntity.ok(facade.getMyShoppingCart());
    }
}
