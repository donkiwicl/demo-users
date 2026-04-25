package cl.duoc.kiosco.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 100, unique = true, nullable = false)
    @NotBlank(message = "Debe enviarse un email como usuario")
    private String email;
    @Column(length = 255, nullable = false)
    @NotBlank(message = "Debe enviarse un password valido")
    private String password;
    @NotNull(message = "Debe indicarse si esta activo o no")
    @Column(nullable = false)
    private boolean active;
}
