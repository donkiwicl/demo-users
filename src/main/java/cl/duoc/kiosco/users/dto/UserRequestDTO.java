package cl.duoc.kiosco.users.dto;

import cl.duoc.kiosco.users.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = "Debe enviarse un email como usuario")
    private String email;

    @NotBlank(message = "Debe enviarse un password valido")
    private String password;

    @NotNull(message = "Debe indicarse si esta activo o no")
    private Boolean active;

    @NotNull(message = "Debe indicarse el rol del usuario")
    private UserRole role;
}
