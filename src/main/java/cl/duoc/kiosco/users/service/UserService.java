package cl.duoc.kiosco.users.service;

import cl.duoc.kiosco.users.model.User;
import cl.duoc.kiosco.users.dto.UserDTO;
import cl.duoc.kiosco.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDTO makeUser(UserDTO newUser){
        User user = new User(0, newUser.getEmail(), "password",true);
        user = userRepository.save(user);
        return new UserDTO(user.getEmail());
    }

    public UserDTO updateUser(long id, UserDTO updateUser){
        User user = userRepository.findById(id).get();
        user.setEmail(updateUser.getEmail());
        user = userRepository.save(user);
        return new UserDTO(user.getEmail());
    }

    public List<UserDTO> findAllUsers(){
        List<UserDTO> userDTOList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            userDTOList.add(new UserDTO(user.getEmail()));
        }
        return userDTOList;
    }

    public UserDTO findUserById(long id){
        User user = userRepository.findById(id).get();
        return new UserDTO(user.getEmail());
    }

    public void deleteUser(long id){
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
    }
}
