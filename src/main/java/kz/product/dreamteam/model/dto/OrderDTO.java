package kz.product.dreamteam.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {

    private ObjectId id;
    private UserDTO user;
    private List<ProductDTO> productDTOList;
    private BigDecimal sum;
}
