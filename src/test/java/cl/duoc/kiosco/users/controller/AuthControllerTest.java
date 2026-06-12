package cl.duoc.kiosco.users.controller;

import cl.duoc.kiosco.users.dto.AuthLoginRequestDTO;
import cl.duoc.kiosco.users.dto.AuthResponseDTO;
import cl.duoc.kiosco.users.model.User;
import cl.duoc.kiosco.users.model.UserRole;
import cl.duoc.kiosco.users.repository.UserRepository;
import cl.duoc.kiosco.users.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de {@link AuthController#login}: autentica con dependencias
 * mockeadas y verifica el token y los datos devueltos en {@link AuthResponseDTO}.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_devuelveTokenCuandoCredencialesValidas() {
        AuthLoginRequestDTO request = new AuthLoginRequestDTO("admin@kiosco.cl", "admin123");
        Authentication auth = new UsernamePasswordAuthenticationToken("admin@kiosco.cl", "admin123");

        User user = new User();
        user.setId(1L);
        user.setEmail("admin@kiosco.cl");
        user.setPassword("hashed");
        user.setActive(true);
        user.setRole(UserRole.ADMIN);

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userRepository.findByEmail("admin@kiosco.cl")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("fake.jwt.token");
        when(jwtService.getExpirationMs()).thenReturn(86400000L);

        AuthResponseDTO response = authController.login(request);

        assertEquals("fake.jwt.token", response.getAccessToken());
        assertEquals(86400000L, response.getExpiresIn());
        assertEquals("admin@kiosco.cl", response.getEmail());
        assertEquals(UserRole.ADMIN, response.getRole());
        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    void login_lanzaIllegalStateCuandoUsuarioAutenticadoNoExiste() {
        AuthLoginRequestDTO request = new AuthLoginRequestDTO("ghost@kiosco.cl", "pass");
        Authentication auth = new UsernamePasswordAuthenticationToken("ghost@kiosco.cl", "pass");

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userRepository.findByEmail("ghost@kiosco.cl")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> authController.login(request));
    }
}
