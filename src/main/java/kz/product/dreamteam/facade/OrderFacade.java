package kz.product.dreamteam.facade;

import kz.product.dreamteam.model.dto.OrderDTO;
import kz.product.dreamteam.model.dto.OrderSaveDTO;

public interface OrderFacade {

    OrderDTO makeOrder(OrderSaveDTO orderSaveDTO);
}
