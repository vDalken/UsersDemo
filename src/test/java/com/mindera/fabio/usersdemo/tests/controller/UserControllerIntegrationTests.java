package com.mindera.fabio.usersdemo.tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.fabio.usersdemo.exceptions.UserNotFoundException;
import com.mindera.fabio.usersdemo.interfaces.UsersRepository;
import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.services.UserService;
import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private User sampleUser1;
    private User sampleUser2;
    private User sampleUser3;
    List<User> sampleUsers;
    private User userToCreate;
    private User existingUser;
    private User nonExistingUser;
    private Long nonExistingUserId;

    @BeforeEach
    public void init() {
        sampleUser1 = User.builder().id(1L).name("marlao").password("1pow").build();
        sampleUser2 = User.builder().id(9L).name("mima").password("youwww").build();
        sampleUser3 = User.builder().id(3L).name("weiza").password("UAUUU").build();
        sampleUsers = List.of(sampleUser1,sampleUser2,sampleUser3);

        userToCreate = User.builder().name("fabio").password("123").build();

        sampleUsers = List.of(
                User.builder().id(5L).name("rita").password("LOOGOOO").build(),
                User.builder().id(6L).name("joao").password("rfierrs").build(),
                User.builder().id(7L).name("mara").password("fsdsa").build()
        );

        existingUser = User.builder().id(1L).name("oldName").password("oldPass").build();
        nonExistingUser = User.builder().id(910L).name("naoexiste").password("piwu").build();

        nonExistingUserId = usersRepository.findMaxUserId()+1;
    }

    @Test
    void createUser_ReturnCreated() throws Exception {
        userService.createUser(sampleUser1);

        ResultActions response = mockMvc.perform(post("/user") //post method meaning it's a post request with the path to the method
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleUser1))); //user sample

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(sampleUser1.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(sampleUser1.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(sampleUser1.getPassword())));
    }

    @Test
    void createUser_ReturnsCreatedUser() {
        userService.createUser(userToCreate);

        User createdUser = usersRepository.findById(userToCreate.getId()).orElseThrow(() -> new RuntimeException("user not found"));

        assertNotNull(createdUser);
        assertEquals(userToCreate,createdUser);
    }

    @Test
    void getAllUsers_ReturnExpectedUsers() throws Exception{
        userService.getAllUsers();

        ResultActions response = mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleUsers)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllUsers_ReturnAllUsers() {
        List<User> users = usersRepository.findAll();

        List<User> result = userService.getAllUsers();

        assertEquals(users, result);
    }

    @Test
    void getUserById_ReturnUser() throws Exception {
        userService.createUser(sampleUser1);
        Long returnedUserId = sampleUser1.getId();
        User returnedUser = userService.getUserById(returnedUserId);

        ResultActions response = mockMvc.perform(get("/user/{userID}", returnedUserId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",CoreMatchers.is(returnedUser.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name",CoreMatchers.is(returnedUser.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password",CoreMatchers.is(returnedUser.getPassword())));
    }

    @Test
    void getUserById_ExistingUserId_ReturnsUser() {
        usersRepository.save(sampleUser1);

        User result = userService.getUserById(sampleUser1.getId());

        assertEquals(sampleUser1, result);
    }

    @Test
    void getUserById_NonExistingUserId_ThrowUserNotFoundException() {
        usersRepository.findById(nonExistingUserId);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(nonExistingUserId));
    }

    @Test
    void updateUser_ReturnUpdatedUser() throws Exception{

        User newUser = userService.createUser(sampleUser2);

        newUser.setName("bixoooo");
        newUser.setPassword("credoooo");

        User updatedUser = userService.updateUser(newUser);

        ResultActions response = mockMvc.perform(put("/user/{id}", newUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",CoreMatchers.is(updatedUser.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(updatedUser.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password",CoreMatchers.is(updatedUser.getPassword())));
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
    void deleteUser_ReturnDeletedUser() throws Exception{
        userService.createUser(sampleUser1);
        Long sampleUser1Id = sampleUser1.getId();


        ResultActions response = mockMvc.perform(delete("/user/{id}",sampleUser1Id)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteUser_ExistingUserId_ReturnDeletedUser() {
        usersRepository.save(existingUser);
        Long existingUserId = existingUser.getId();

        User result = userService.deleteUser(existingUserId);

        assertNotNull(usersRepository.findById(existingUserId));
        assertEquals(existingUser, result);
    }

    @Test
    void deleteUser_NonExistingUserId_ThrowUserNotFoundException() {
        usersRepository.findById(nonExistingUserId);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(nonExistingUserId));
    }
}
