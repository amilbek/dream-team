package kz.product.dreamteam.facade.impl;

import kz.product.dreamteam.facade.ProductFacade;
import kz.product.dreamteam.model.dto.ProductDTO;
import kz.product.dreamteam.model.dto.ProductSaveDTO;
import kz.product.dreamteam.model.dto.request.*;
import kz.product.dreamteam.model.entity.OrderPosition;
import kz.product.dreamteam.model.entity.Product;
import kz.product.dreamteam.model.entity.User;
import kz.product.dreamteam.model.entity.enums.Role;
import kz.product.dreamteam.service.OrderService;
import kz.product.dreamteam.service.ProductService;
import kz.product.dreamteam.service.UserService;
import kz.product.dreamteam.utils.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService service;
    private final UserService userService;
    private final OrderService orderService;

    @Override
    public ProductDTO saveProduct(ProductSaveDTO productSaveDTO) {
        Product product = ModelMapperUtil.map(productSaveDTO, Product.class);
        product.setLikes(0);
        product.setViews(0);
        product.setIsDeleted(true);
        return ModelMapperUtil.map(service.save(product), ProductDTO.class);
    }

    @Override
    public ProductDTO getProduct(ObjectId id) {
        User user = userService.getUser();
        Product product = service.getProduct(id);
        if (user.getRole().equals(Role.USER)) {
            product.setViews(product.getViews() + 1);
            product.setViewedUsers(Collections.singletonList(user));
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
        User user = userService.getUser();
        Product product = service.getProduct(id);
        if (user.getRole().equals(Role.USER)) {
            product.setLikes(product.getLikes() + 1);
            product.setLikedUsers(Collections.singletonList(user));
        }
        Product savedProduct = service.save(product);
        return ModelMapperUtil.map(savedProduct, ProductDTO.class);
    }

    @Override
    public Collection<ProductDTO> search(SearchRequest<FilterRequest, SortRequest> searchRequest) {
        return service.search(searchRequest)
                .stream()
                .map(x -> ModelMapperUtil.map(x, ProductDTO.class)).toList();
    }

    @Override
    public Collection<ProductDTO> recommendedList() {
        User user = userService.getUser();
        List<Product> orderProductsByUser = orderService.getOrdersByUser(user.getId())
                .stream()
                .flatMap(order -> order.getOrderPositions().stream())
                .map(OrderPosition::getProduct).toList();
        List<String> productCategories = orderProductsByUser
                .stream()
                .map(Product::getCategory)
                .distinct().toList();
        BigDecimal maxProductPrice = orderProductsByUser
                .stream()
                .map(Product::getPrice)
                .max(BigDecimal::compareTo).orElse(BigDecimal.valueOf(0));
        BigDecimal minProductPrice = orderProductsByUser
                .stream()
                .map(Product::getPrice)
                .min(BigDecimal::compareTo).orElse(BigDecimal.valueOf(0));
        List<Product> recommendedListByCategories = service.getAllByCategoriesIn(productCategories);
        List<Product> recommendedListByPrice = service.getAllByPricesBetween(minProductPrice, maxProductPrice);
        List<Product> likedProductsByUser = service.getProductsByLikedUser(user.getId());
        List<String> likedProductCategoriesByUser = likedProductsByUser
                .stream()
                .map(Product::getCategory)
                .distinct()
                .toList();
        List<Product> recommendedListByLikedProductCategories = service.getAllByCategoriesIn(likedProductCategoriesByUser);
        List<Product> viewedProductsByUser = service.getProductsByViewedUser(user.getId());
        List<String> viewedProductCategoriesByUser = viewedProductsByUser
                .stream()
                .map(Product::getCategory)
                .distinct()
                .toList();
        List<Product> recommendedListByViewedProductCategories = service.getAllByCategoriesIn(viewedProductCategoriesByUser);
        List<Product> allSortedLikedProducts = service.getAllProducts()
                .stream()
                .sorted(Comparator.comparing(Product::getLikes).reversed()).toList();
        List<Product> allSortedViewedProducts = service.getAllProducts()
                .stream()
                .sorted(Comparator.comparing(Product::getLikes).reversed()).toList();
        List<Product> recommendedList = new ArrayList<>(recommendedListByCategories);

        checkAndAddToRecommendedList(recommendedListByPrice, recommendedList);
        checkAndAddToRecommendedList(recommendedListByLikedProductCategories, recommendedList);
        checkAndAddToRecommendedList(recommendedListByViewedProductCategories, recommendedList);
        checkAndAddToRecommendedList(likedProductsByUser, recommendedList);
        checkAndAddToRecommendedList(viewedProductsByUser, recommendedList);
        checkAndAddToRecommendedList(allSortedLikedProducts, recommendedList);
        checkAndAddToRecommendedList(allSortedViewedProducts, recommendedList);

        return recommendedList
                .stream()
                .map(x -> ModelMapperUtil.map(x, ProductDTO.class))
                .toList();
    }

    private void checkAndAddToRecommendedList(List<Product> products, List<Product> recommends) {
        for (Product product : products) {
            if (!recommends.contains(product)) {
                recommends.add(product);
            }
        }
    }
}
