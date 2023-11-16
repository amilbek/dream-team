package kz.product.dreamteam.facade;

import kz.product.dreamteam.model.dto.OrderDTO;
import kz.product.dreamteam.model.dto.OrderPositionSaveDTO;
import kz.product.dreamteam.model.dto.request.*;
import org.bson.types.ObjectId;

import java.util.Collection;

public interface OrderFacade {

    OrderDTO addToShoppingCart(OrderPositionSaveDTO orderPositionSaveDTO);

    OrderDTO removeFromShoppingCart(OrderPositionSaveDTO orderPositionSaveDTO);

    OrderDTO makeOrder(ObjectId id);

    Collection<OrderDTO> search(SearchRequest<FilterRequest, SortRequest> request);

    OrderDTO getMyShoppingCart();

    OrderDTO getMyOrders();
}
