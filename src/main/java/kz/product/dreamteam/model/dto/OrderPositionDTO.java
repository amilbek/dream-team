package kz.product.dreamteam.model.dto;

import lombok.Data;

@Data
public class OrderPositionDTO {

    private String id;
    private OrderDTO order;
    private ProductDTO product;
    private Integer count;
}
