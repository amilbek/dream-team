package kz.product.dreamteam.model.dto;

import lombok.Data;
import org.bson.types.Decimal128;

import java.math.BigDecimal;

@Data
public class ProductSaveDTO {

    private String id;
    private String name;
    private String description;
    private String category;
    private Decimal128 price;
}
