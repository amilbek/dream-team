package kz.product.dreamteam.model.dto;

import kz.product.dreamteam.model.entity.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {

    private String id;
    private UserDTO user;
    private List<OrderPositionDTO> orderPositions;
    private BigDecimal totalSum;
    private LocalDateTime createdAt;
    private OrderStatus orderStatus;
}
