package cl.duoc.kiosco.users.service;

import cl.duoc.kiosco.users.dto.UserResponseDTO;
import cl.duoc.kiosco.users.dto.UserRequestDTO;
import cl.duoc.kiosco.users.exception.EmailAlreadyExistsException;
import cl.duoc.kiosco.users.exception.ResourceNotFoundException;
import cl.duoc.kiosco.users.model.User;
import cl.duoc.kiosco.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserResponseDTO makeToUserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getEmail(), user.isActive(), user.getRole());
    }

    public UserResponseDTO makeUser(UserRequestDTO newUser){
        log.info("Se inicia la creación de usuario: {}", newUser.getEmail());
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }
        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setActive(newUser.getActive());
        user.setRole(newUser.getRole());
        user = userRepository.save(user);
        return makeToUserResponseDTO(user);
    }

    public UserResponseDTO updateUser(long id, UserRequestDTO updateUser){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        user.setEmail(updateUser.getEmail());
        user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        user.setActive(updateUser.getActive());
        user.setRole(updateUser.getRole());
        user = userRepository.save(user);
        return makeToUserResponseDTO(user);
    }

    public List<UserResponseDTO> findAllUsers(){
        return userRepository.findAll().stream().map(this::makeToUserResponseDTO).collect(Collectors.toList());
    }

    public UserResponseDTO findUserById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return makeToUserResponseDTO(user);
    }

    public void deleteUser(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        userRepository.delete(user);
    }
}
