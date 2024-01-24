package com.mindera.fabio.usersdemo.services;

import com.mindera.fabio.usersdemo.interfaces.UsersRepository;
import com.mindera.fabio.usersdemo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User createUser(final User user) {
        return usersRepository.save(user);
    }

    public User getUserById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    public User updateUser(User user) {
        if (usersRepository.existsById(user.getId())) {
            return usersRepository.save(user);
        }
        return null;
    }

    public User deleteUser(Long id) {
        Optional<User> userToBeDeleted = usersRepository.findById(id);
        userToBeDeleted.ifPresent(usersRepository::delete);
        return userToBeDeleted.orElse(null);
    }
}
