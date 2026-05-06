package cl.duoc.kiosco.users.service;

import cl.duoc.kiosco.users.dto.UserResponseDTO;
import cl.duoc.kiosco.users.model.User;
import cl.duoc.kiosco.users.dto.UserRequestDTO;
import cl.duoc.kiosco.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    private UserResponseDTO makeToUserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getEmail());
    }

    public UserResponseDTO makeUser(UserRequestDTO newUser){
        log.error("Se inicia la creación de la entidad de respaldo");
        User user = new User(0, newUser.getEmail(), "password",true);
        user = userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getEmail());
    }

    public UserResponseDTO updateUser(long id, UserRequestDTO updateUser){
        User user = userRepository.findById(id).get();
        user.setEmail(updateUser.getEmail());
        user = userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getEmail());
    }

    public List<UserResponseDTO> findAllUsers(){
        return userRepository.findAll().stream().map(this::makeToUserResponseDTO).collect(Collectors.toList());
    }

    public UserRequestDTO findUserById(long id){
        User user = userRepository.findById(id).get();
        return new UserRequestDTO(user.getEmail());
    }

    public void deleteUser(long id){
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
    }
}
