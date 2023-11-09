package kz.product.dreamteam.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Data
public class ProductSaveDTO {

    private ObjectId id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
}
