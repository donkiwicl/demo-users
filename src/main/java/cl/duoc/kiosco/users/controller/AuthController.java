package cl.duoc.kiosco.users.controller;
import cl.duoc.kiosco.users.dto.AuthLoginRequestDTO;
import cl.duoc.kiosco.users.dto.AuthResponseDTO;
import cl.duoc.kiosco.users.model.User;
import cl.duoc.kiosco.users.repository.UserRepository;
import cl.duoc.kiosco.users.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody AuthLoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado"));
        return new AuthResponseDTO(
                jwtService.generateToken(user),
                jwtService.getExpirationMs(),
                user.getEmail(),
                user.getRole()
        );
    }
}
