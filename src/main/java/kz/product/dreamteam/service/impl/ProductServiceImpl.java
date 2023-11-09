package kz.product.dreamteam.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.product.dreamteam.model.entity.Product;
import kz.product.dreamteam.repository.ProductRepository;
import kz.product.dreamteam.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    public Product getProduct(ObjectId id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }
}
