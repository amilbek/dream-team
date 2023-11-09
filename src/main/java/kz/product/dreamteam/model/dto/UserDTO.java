package kz.product.dreamteam.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
}
