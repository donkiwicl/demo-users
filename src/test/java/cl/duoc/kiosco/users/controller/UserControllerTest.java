package cl.duoc.kiosco.users.controller;

import cl.duoc.kiosco.users.dto.UserRequestDTO;
import cl.duoc.kiosco.users.dto.UserResponseDTO;
import cl.duoc.kiosco.users.model.UserRole;
import cl.duoc.kiosco.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de {@link UserController}: invoca los métodos directamente con
 * un {@link UserService} mockeado y verifica el {@link ResponseEntity} resultante.
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponseDTO sampleResponse() {
        return new UserResponseDTO(1L, "user@kiosco.cl", true, UserRole.USER);
    }

    @Test
    void getUsers_devuelve200ConLista() {
        when(userService.findAllUsers()).thenReturn(List.of(sampleResponse()));

        ResponseEntity<List<UserResponseDTO>> response = userController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void postUser_devuelve201ConUsuarioCreado() {
        UserRequestDTO request = new UserRequestDTO("user@kiosco.cl", "secret123", true, UserRole.USER);
        when(userService.makeUser(request)).thenReturn(sampleResponse());

        ResponseEntity<UserResponseDTO> response = userController.postUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("user@kiosco.cl", response.getBody().getEmail());
    }

    @Test
    void getUser_devuelve200ConUsuario() {
        when(userService.findUserById(1L)).thenReturn(sampleResponse());

        ResponseEntity<UserResponseDTO> response = userController.getUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void putUser_devuelve200ConUsuarioActualizado() {
        UserRequestDTO request = new UserRequestDTO("user@kiosco.cl", "secret123", true, UserRole.USER);
        when(userService.updateUser(1L, request)).thenReturn(sampleResponse());

        ResponseEntity<UserResponseDTO> response = userController.putUser(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user@kiosco.cl", response.getBody().getEmail());
    }

    @Test
    void deleteUser_devuelve204() {
        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(1L);
    }
}
