package kz.product.dreamteam.controller;

import kz.product.dreamteam.facade.ProductFacade;
import kz.product.dreamteam.model.dto.ProductDTO;
import kz.product.dreamteam.model.dto.ProductSaveDTO;
import kz.product.dreamteam.model.dto.request.FilterRequest;
import kz.product.dreamteam.model.dto.request.SortRequest;
import kz.product.dreamteam.model.dto.request.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductFacade facade;

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> save(@RequestBody ProductSaveDTO productSaveDTO) {
        return new ResponseEntity<>(facade.saveProduct(productSaveDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> get(@PathVariable("id") ObjectId id) {
        return ResponseEntity.ok(facade.getProduct(id));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ProductDTO> edit(@PathVariable("id") ObjectId id, @RequestBody ProductSaveDTO productSaveDTO) {
        return ResponseEntity.ok(facade.editProduct(id, productSaveDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") ObjectId id) {
        facade.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<ProductDTO> like(@PathVariable("id") ObjectId id) {
        return ResponseEntity.ok(facade.likeProduct(id));
    }

    @PostMapping("/search")
    public ResponseEntity<Collection<ProductDTO>> search(@RequestBody SearchRequest<FilterRequest, SortRequest> request) {
        return ResponseEntity.ok(facade.search(request));
    }

    @PostMapping("/recommendation")
    public ResponseEntity<Collection<ProductDTO>> recommendation() {
        return ResponseEntity.ok(facade.recommendedList());
    }
}
