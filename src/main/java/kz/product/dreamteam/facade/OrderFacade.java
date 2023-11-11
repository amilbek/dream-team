package kz.product.dreamteam.facade;

import kz.product.dreamteam.model.dto.OrderDTO;
import kz.product.dreamteam.model.dto.OrderSaveDTO;
import kz.product.dreamteam.model.dto.request.*;
import org.bson.types.ObjectId;

import java.util.Collection;

public interface OrderFacade {

    OrderDTO addToShoppingCart(OrderSaveDTO orderSaveDTO);

    OrderDTO makeOrder(ObjectId id);

    Collection<OrderDTO> search(SearchRequest<FilterRequest, SortRequest> request);
}
