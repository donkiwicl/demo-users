package cl.duoc.kiosco.users.service;

import cl.duoc.kiosco.users.dto.UserResponseDTO;
import cl.duoc.kiosco.users.model.User;
import cl.duoc.kiosco.users.dto.UserRequestDTO;
import cl.duoc.kiosco.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    private UserResponseDTO makeToUserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getEmail());
    }

    public UserResponseDTO makeUser(UserRequestDTO newUser){
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
