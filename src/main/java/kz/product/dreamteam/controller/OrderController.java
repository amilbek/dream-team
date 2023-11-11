package kz.product.dreamteam.controller;

import kz.product.dreamteam.facade.OrderFacade;
import kz.product.dreamteam.model.dto.*;
import kz.product.dreamteam.model.dto.request.FilterRequest;
import kz.product.dreamteam.model.dto.request.SortRequest;
import kz.product.dreamteam.model.dto.request.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade facade;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add-to-shopping-card")
    public ResponseEntity<OrderDTO> addToShoppingCard(@RequestBody OrderSaveDTO orderSaveDTO) {
        return ResponseEntity.ok(facade.addToShoppingCart(orderSaveDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/make-order/{id}")
    public ResponseEntity<OrderDTO> makeOrder(@PathVariable ObjectId id) {
        return ResponseEntity.ok(facade.makeOrder(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<Collection<OrderDTO>> search(@RequestBody SearchRequest<FilterRequest, SortRequest> request) {
        return ResponseEntity.ok(facade.search(request));
    }
}
