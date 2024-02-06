package com.mindera.fabio.usersdemo.integration_tests.service;

import com.mindera.fabio.usersdemo.exceptions.UserNotFoundException;
import com.mindera.fabio.usersdemo.interfaces.UsersRepository;
import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class UserServiceTests {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserService userService;
    private User createdUser;
    private User userToCreate;
    private User sampleUser;
    private List<User> sampleUsers;
    private User existingUser;
    private User updatedUser;
    private User nonExistingUser;
    private Long nonExistingUserId;

    @BeforeEach
    void init() {
        userToCreate = User.builder().name("fabio").password("123").build();
        createdUser = User.builder().id(1L).name("fabio").password("123").build();
        sampleUser = User.builder().id(2L).name("rui").password("231").build();

        sampleUsers = List.of(
                User.builder().id(5L).name("rita").password("LOOGOOO").build(),
                User.builder().id(6L).name("joao").password("rfierrs").build(),
                User.builder().id(7L).name("mara").password("fsdsa").build()
        );

        existingUser = User.builder().id(1L).name("oldName").password("oldPass").build();
        updatedUser = User.builder().id(1L).name("newName").password("newPass").build();
        nonExistingUser = User.builder().id(910L).name("naoexiste").password("piwu").build();

        nonExistingUserId = usersRepository.findMaxUserId()+1;
    }

    @Test
    void createUser_ReturnsCreatedUser() {
        userService.createUser(userToCreate);

        User createdUser = usersRepository.findById(userToCreate.getId()).orElseThrow(() -> new RuntimeException("user not found"));

        assertNotNull(createdUser);
        assertEquals(userToCreate.getName(),createdUser.getName());
        assertEquals(userToCreate.getPassword(),createdUser.getPassword());
    }

    @Test
    void getUserById_ExistingUserId_ReturnsUser() {
        usersRepository.save(sampleUser);

        User result = userService.getUserById(sampleUser.getId());

        assertEquals(sampleUser.getId(), result.getId());
        assertEquals(sampleUser.getName(),result.getName());
        assertEquals(sampleUser.getPassword(),result.getPassword());
    }

    @Test
    void getUserById_NonExistingUserId_ThrowUserNotFoundException() {
        usersRepository.findById(nonExistingUserId);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(nonExistingUserId));
    }

    @Test
    void getAllUsers_ReturnAllUsers() {
        List<User> users = usersRepository.findAll();

        List<User> result = userService.getAllUsers();

        assertEquals(users, result);
    }

    @Test
    void updateUser_ExistingUserId_ReturnUpdatedUser() {
        Long maxUserId = usersRepository.findMaxUserId();
        User newUser = usersRepository.findById(maxUserId).orElseThrow(UserNotFoundException::new);

        newUser.setName("mixaaaaaa");
        newUser.setPassword("miauuuuuu");

        User result = userService.updateUser(newUser);

        Assertions.assertTrue(usersRepository.existsById(maxUserId));
        assertEquals(newUser, result);
    }

    @Test
    void updateUser_NonExistingUserId_ThrowUserNotFoundException() {
        Assertions.assertFalse(usersRepository.existsById(usersRepository.findMaxUserId()+1));
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(nonExistingUser));
    }

    @Test
    void deleteUser_ExistingUserId_ReturnDeletedUser() {
        usersRepository.save(existingUser);
        Long existingUserId = existingUser.getId();

        User result = userService.deleteUser(existingUserId);

        assertNotNull(usersRepository.findById(existingUserId));
        assertEquals(existingUser.getId(), result.getId());
        assertEquals(existingUser.getName(), result.getName());
        assertEquals(existingUser.getPassword(), result.getPassword());
    }

    @Test
    void deleteUser_NonExistingUserId_ThrowUserNotFoundException() {
        usersRepository.findById(nonExistingUserId);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(nonExistingUserId));
    }
}
