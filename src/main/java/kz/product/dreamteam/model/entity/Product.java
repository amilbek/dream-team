package kz.product.dreamteam.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("isDeleted is false")
@Document(collection = "products")
public class Product {

    @Id
    private ObjectId id;
    private String name;
    private String description;
    private String category;
    private Decimal128 price;
    private Boolean isDeleted;
    private Integer views;
    private Integer likes;

    @OneToMany(fetch = FetchType.LAZY)
    private List<User> likedUsers;

    @OneToMany(fetch = FetchType.LAZY)
    private List<User> viewedUsers;
}
