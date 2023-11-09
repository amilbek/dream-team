package kz.product.dreamteam.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@Data
public class UserDTO {

    private ObjectId id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
}
