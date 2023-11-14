package kz.product.dreamteam.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class OrderPosition {

    @Id
    private ObjectId id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Integer count;
}
