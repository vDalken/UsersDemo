package com.mindera.fabio.usersdemo.restcontrollers;

import com.mindera.fabio.usersdemo.exceptions.UserDoesNotMatchException;
import com.mindera.fabio.usersdemo.exceptions.UserFieldsCannotBeNullOrEmptyException;
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
        return service.getUserById(userID);
    }

    @PostMapping //indicates that this method handles post requests
    public User createUser(@RequestBody User user){
        return service.createUser(user);
    }

    @GetMapping //indicates that this method handles get requests
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }

    @PutMapping("/{userId}") //indicates that this method handles put requests, with a placeholder variable
    public User updateUser(@PathVariable Long userId, @RequestBody User user){
        if(!Objects.equals(userId, user.getId())){
            throw new UserDoesNotMatchException();
        }
        if (user.getId() == null || user.getName() == null || user.getName().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty() || user.getAddress() == null) {
            throw new UserFieldsCannotBeNullOrEmptyException();
        }
        return service.updateUser(user);
    }

    @DeleteMapping("/{id}") //indicates that this method handles delete requests
    public User deleteUser(@PathVariable Long id){
        return service.deleteUser(id);
    }

}
