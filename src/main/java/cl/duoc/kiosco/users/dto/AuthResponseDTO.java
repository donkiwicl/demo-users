package cl.duoc.kiosco.users.dto;

import cl.duoc.kiosco.users.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String tokenType = "Bearer";
    private String accessToken;
    private long expiresIn;
    private String email;
    private UserRole role;

    public AuthResponseDTO(String accessToken, long expiresIn, String email, UserRole role) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.email = email;
        this.role = role;
    }
}

