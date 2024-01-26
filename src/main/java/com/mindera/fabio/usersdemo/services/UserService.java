package com.mindera.fabio.usersdemo.services;

import com.mindera.fabio.usersdemo.exceptions.UserNotFoundException;
import com.mindera.fabio.usersdemo.interfaces.UsersRepository;
import com.mindera.fabio.usersdemo.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor //injects dependencies of all final fields of the class
@Service //tells Spring to instanciate this class when needed
public class UserService {

    private final UsersRepository usersRepository;

    public User createUser(final User user) {
        return usersRepository.save(user);
    }

    public User getUserById(Long userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
    }

    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    public User updateUser(User user) {
        if (usersRepository.existsById(user.getId())) {
            return usersRepository.save(user);
        }
        throw new UserNotFoundException();
    }

    public User deleteUser(Long userId) {
        Optional<User> userToBeDeleted = usersRepository.findById(userId);
        userToBeDeleted.ifPresent(usersRepository::delete);
        return userToBeDeleted.orElseThrow(() -> new UserNotFoundException());
    }
}
