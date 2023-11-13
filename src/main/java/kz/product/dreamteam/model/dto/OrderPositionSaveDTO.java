package kz.product.dreamteam.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class OrderPositionSaveDTO {

    private ObjectId productId;
}
