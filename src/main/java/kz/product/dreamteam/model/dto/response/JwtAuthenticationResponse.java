package kz.product.dreamteam.model.dto.response;

import kz.product.dreamteam.model.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String token;
    private Role userRole;
    private String email;
    private String firstName;
    private String lastName;
}
