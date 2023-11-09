package kz.product.dreamteam.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class OrderPositionDTO {

    private ObjectId id;
    private OrderDTO order;
    private ProductDTO product;
    private Integer count;
}
