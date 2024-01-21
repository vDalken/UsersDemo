package com.mindera.fabio.usersdemo.restcontrollers;

import com.mindera.fabio.usersdemo.exceptions.UserDoesNotMatchException;
import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/{userID}")
    public User getUserById(@PathVariable Long userID){
        return service.getUserById(userID);
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return service.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user){
        if(!Objects.equals(id, user.getId())){
            throw new UserDoesNotMatchException();
        }
        return service.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable Long id){
        return service.deleteUser(id);
    }

}
