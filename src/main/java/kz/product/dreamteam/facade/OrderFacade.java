package kz.product.dreamteam.facade;

import kz.product.dreamteam.model.dto.OrderDTO;
import kz.product.dreamteam.model.dto.OrderSaveDTO;
import org.bson.types.ObjectId;

public interface OrderFacade {

    OrderDTO addToShoppingCart(OrderSaveDTO orderSaveDTO);

    OrderDTO makeOrder(ObjectId id);
}
