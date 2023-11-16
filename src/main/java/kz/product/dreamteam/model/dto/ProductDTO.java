package kz.product.dreamteam.model.dto;

import lombok.Data;
import org.bson.types.Decimal128;

@Data
public class ProductDTO {

    private String id;
    private String name;
    private String description;
    private String category;
    private Decimal128 price;
    private Boolean isDeleted;
    private Integer views;
    private Integer likes;
}
