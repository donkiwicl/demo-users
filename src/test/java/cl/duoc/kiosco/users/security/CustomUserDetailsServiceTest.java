package cl.duoc.kiosco.users.security;

import cl.duoc.kiosco.users.model.User;
import cl.duoc.kiosco.users.model.UserRole;
import cl.duoc.kiosco.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de {@link CustomUserDetailsService}: construcción del
 * {@link UserDetails} a partir de la entidad y manejo del usuario inexistente.
 */
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_devuelveUserDetailsConRolYEstado() {
        User user = new User();
        user.setEmail("admin@kiosco.cl");
        user.setPassword("hashed");
        user.setActive(true);
        user.setRole(UserRole.ADMIN);
        when(userRepository.findByEmail("admin@kiosco.cl")).thenReturn(Optional.of(user));

        UserDetails details = customUserDetailsService.loadUserByUsername("admin@kiosco.cl");

        assertEquals("admin@kiosco.cl", details.getUsername());
        assertEquals("hashed", details.getPassword());
        assertTrue(details.isEnabled());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_usuarioInactivoQuedaDeshabilitado() {
        User user = new User();
        user.setEmail("inactivo@kiosco.cl");
        user.setPassword("hashed");
        user.setActive(false);
        user.setRole(UserRole.USER);
        when(userRepository.findByEmail("inactivo@kiosco.cl")).thenReturn(Optional.of(user));

        UserDetails details = customUserDetailsService.loadUserByUsername("inactivo@kiosco.cl");

        assertFalse(details.isEnabled());
    }

    @Test
    void loadUserByUsername_lanzaUsernameNotFoundCuandoNoExiste() {
        when(userRepository.findByEmail("ghost@kiosco.cl")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("ghost@kiosco.cl"));
    }
}
