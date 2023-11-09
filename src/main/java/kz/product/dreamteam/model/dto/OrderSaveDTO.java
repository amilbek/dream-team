package kz.product.dreamteam.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderSaveDTO {

    private List<OrderPositionSaveDTO> orderPositions;
}
