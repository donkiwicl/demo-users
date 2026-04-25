package cl.duoc.kiosco.users.service;

import cl.duoc.kiosco.users.model.User;
import cl.duoc.kiosco.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User makeUser(User newUser){
        return userRepository.save(newUser);
    }

    public User updateUser(User updateUser){
        return userRepository.save(updateUser);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findUserById(long id){
        return userRepository.findById(id).get();
    }

    public void deleteUser(long id){
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
    }
}
