package kz.product.dreamteam.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSaveDTO {

    private String id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
}
