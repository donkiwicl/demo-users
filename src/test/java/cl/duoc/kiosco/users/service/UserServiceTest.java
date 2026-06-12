package cl.duoc.kiosco.users.service;

import cl.duoc.kiosco.users.dto.UserRequestDTO;
import cl.duoc.kiosco.users.dto.UserResponseDTO;
import cl.duoc.kiosco.users.exception.EmailAlreadyExistsException;
import cl.duoc.kiosco.users.exception.ResourceNotFoundException;
import cl.duoc.kiosco.users.model.User;
import cl.duoc.kiosco.users.model.UserRole;
import cl.duoc.kiosco.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de {@link UserService} con dependencias mockeadas
 * ({@link UserRepository} y {@link PasswordEncoder}), sin contexto de Spring.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO request;

    @BeforeEach
    void setUp() {
        request = new UserRequestDTO("nuevo@kiosco.cl", "secret123", true, UserRole.USER);
    }

    private User existingUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("existe@kiosco.cl");
        user.setPassword("hashed");
        user.setActive(true);
        user.setRole(UserRole.ADMIN);
        return user;
    }

    @Test
    void makeUser_creaUsuarioCuandoEmailNoExiste() {
        when(userRepository.existsByEmail("nuevo@kiosco.cl")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User toSave = invocation.getArgument(0);
            toSave.setId(10L);
            return toSave;
        });

        UserResponseDTO response = userService.makeUser(request);

        assertEquals(10L, response.getId());
        assertEquals("nuevo@kiosco.cl", response.getEmail());
        assertTrue(response.isActive());
        assertEquals(UserRole.USER, response.getRole());

        // La contraseña se guarda cifrada, nunca en texto plano.
        verify(passwordEncoder).encode("secret123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void makeUser_lanzaEmailAlreadyExistsCuandoEmailDuplicado() {
        when(userRepository.existsByEmail("nuevo@kiosco.cl")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.makeUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_actualizaUsuarioExistente() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser()));
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userService.updateUser(1L, request);

        assertEquals("nuevo@kiosco.cl", response.getEmail());
        assertEquals(UserRole.USER, response.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_lanzaResourceNotFoundCuandoNoExiste() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(99L, request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void findAllUsers_devuelveListaMapeada() {
        when(userRepository.findAll()).thenReturn(List.of(existingUser()));

        List<UserResponseDTO> users = userService.findAllUsers();

        assertEquals(1, users.size());
        assertEquals("existe@kiosco.cl", users.get(0).getEmail());
    }

    @Test
    void findUserById_devuelveUsuarioCuandoExiste() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser()));

        UserResponseDTO response = userService.findUserById(1L);

        assertEquals(1L, response.getId());
        assertEquals(UserRole.ADMIN, response.getRole());
    }

    @Test
    void findUserById_lanzaResourceNotFoundCuandoNoExiste() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserById(99L));
    }

    @Test
    void deleteUser_eliminaUsuarioExistente() {
        User user = existingUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_lanzaResourceNotFoundCuandoNoExiste() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(99L));
        verify(userRepository, never()).delete(any());
    }
}
