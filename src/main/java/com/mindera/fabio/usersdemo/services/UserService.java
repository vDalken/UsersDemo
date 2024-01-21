package com.mindera.fabio.usersdemo.services;

import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.repository.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Repository repository;

    public User createUser(final User user){
        return repository.createUser(user);
    }

    public User getUserById(Long id){
        return repository.getUserById(id);
    }

    public List<User> getAllUsers(){
        return repository.getUsers();
    }

    public User updateUser(User user){
        return repository.updateUser(user);
    }

    public User deleteUser(Long id){
        return repository.deleteUser(id);
    }

}
