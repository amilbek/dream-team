package kz.product.dreamteam.model.entity;

import jakarta.persistence.*;
import kz.product.dreamteam.model.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private ObjectId id;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderPosition> orderPositions;
    private Decimal128 totalSum;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
}
