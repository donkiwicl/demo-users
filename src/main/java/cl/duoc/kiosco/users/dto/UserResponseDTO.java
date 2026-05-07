package cl.duoc.kiosco.users.dto;

import cl.duoc.kiosco.users.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private long id;
    private String email;
    private boolean active;
    private UserRole role;
}
