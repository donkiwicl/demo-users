package cl.duoc.kiosco.users.controller;

import cl.duoc.kiosco.users.dto.UserRequestDTO;
import cl.duoc.kiosco.users.dto.UserResponseDTO;
import cl.duoc.kiosco.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST de usuarios. Expone el CRUD bajo /api/v1/users.
 * La autorización (quién puede llamar cada endpoint) se define en SecurityConfig.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> postUser(@Valid @RequestBody UserRequestDTO newUser) {
        UserResponseDTO user = userService.makeUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> putUser(@PathVariable long id, @Valid @RequestBody UserRequestDTO user){
        return ResponseEntity.ok(userService.updateUser(id,user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
