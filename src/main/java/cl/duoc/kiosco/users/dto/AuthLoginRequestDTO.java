package cl.duoc.kiosco.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginRequestDTO {
    @NotBlank(message = "Debe enviarse un email")
    private String email;

    @NotBlank(message = "Debe enviarse un password")
    private String password;
}

