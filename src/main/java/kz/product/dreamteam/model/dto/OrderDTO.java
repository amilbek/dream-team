package kz.product.dreamteam.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {

    private ObjectId id;
    private UserDTO user;
    private List<OrderPositionDTO> orderPositions;
    private BigDecimal totalSum;
    private LocalDateTime createdAt;
}
