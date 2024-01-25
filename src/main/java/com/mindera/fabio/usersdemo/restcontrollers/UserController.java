package com.mindera.fabio.usersdemo.restcontrollers;

import com.mindera.fabio.usersdemo.exceptions.UserDoesNotMatchException;
import com.mindera.fabio.usersdemo.exceptions.UserNotFoundException;
import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user") //tells to where to map the http requests
public class UserController {

    private final UserService service;

    @GetMapping("/{userID}") //it's a get request with variable placeholder
    public User getUserById(@PathVariable Long userID){
        User user = service.getUserById(userID);
        if(user.equals(null)){
            throw new UserNotFoundException();
        }
        return user;
    }

    @PostMapping //indicates that this method handles post requests
    public User createUser(@RequestBody User user){
        return service.createUser(user);
    }

    @GetMapping //indicates that this method handles get requests
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }

    @PutMapping("/{id}") //indicates that this method handles put requests, with a placeholder variable
    public User updateUser(@PathVariable Long id, @RequestBody User user){
        if(!Objects.equals(id, user.getId())){
            throw new UserDoesNotMatchException();
        }
        User newUser = service.updateUser(user);
        if(newUser.equals(null)){
            throw new UserNotFoundException();
        }
        return newUser;
    }

    @DeleteMapping("/{id}") //indicates that this method handles delete requests
    public User deleteUser(@PathVariable Long id){
        return service.deleteUser(id);
    }

}
