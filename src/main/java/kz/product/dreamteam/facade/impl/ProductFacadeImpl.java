package kz.product.dreamteam.facade.impl;

import kz.product.dreamteam.facade.ProductFacade;
import kz.product.dreamteam.model.dto.ProductDTO;
import kz.product.dreamteam.model.dto.ProductSaveDTO;
import kz.product.dreamteam.model.entity.Product;
import kz.product.dreamteam.model.entity.User;
import kz.product.dreamteam.model.entity.enums.Role;
import kz.product.dreamteam.service.ProductService;
import kz.product.dreamteam.service.UserService;
import kz.product.dreamteam.utils.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService service;
    private final UserService userService;

    @Override
    public ProductDTO saveProduct(ProductSaveDTO productSaveDTO) {
        Product product = ModelMapperUtil.map(productSaveDTO, Product.class);
        product.setLikes(0);
        product.setViews(0);
        product.setIsDeleted(false);
        return ModelMapperUtil.map(service.save(product), ProductDTO.class);
    }

    @Override
    public ProductDTO getProduct(ObjectId id) {
        User user = userService.getUser();
        Product product = service.getProduct(id);
        if (user.getRole().equals(Role.USER)) {
            product.setViews(product.getViews() + 1);
        }
        Product savedProduct = service.save(product);
        return ModelMapperUtil.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO editProduct(ObjectId id, ProductSaveDTO productSaveDTO) {
        Product product = service.getProduct(id);
        product.setName(productSaveDTO.getName());
        product.setDescription(productSaveDTO.getDescription());
        product.setCategory(productSaveDTO.getCategory());
        product.setPrice(productSaveDTO.getPrice());
        Product savedProduct = service.save(product);
        return ModelMapperUtil.map(savedProduct, ProductDTO.class);
    }

    @Override
    public void deleteProduct(ObjectId id) {
        Product product = service.getProduct(id);
        product.setIsDeleted(false);
        service.save(product);
    }

    @Override
    public ProductDTO likeProduct(ObjectId id) {
        Product product = service.getProduct(id);
        product.setLikes(product.getLikes() + 1);
        Product savedProduct = service.save(product);
        return ModelMapperUtil.map(savedProduct, ProductDTO.class);
    }
}
