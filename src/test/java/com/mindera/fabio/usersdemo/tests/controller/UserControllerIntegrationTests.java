package com.mindera.fabio.usersdemo.tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.fabio.usersdemo.interfaces.AddressesRepository;
import com.mindera.fabio.usersdemo.interfaces.UsersRepository;
import com.mindera.fabio.usersdemo.model.Address;
import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.services.UserService;
import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc(addFilters=false) //filters like security won't be applied to the tests
class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AddressesRepository addressesRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private User sampleUser1;
    List<User> sampleUsers;
    private User userToCreate;
    private User existingUser;
    private Long nonExistingUserId;
    private User userWithNullFields;

    @BeforeEach
    public void init() {
        Long nonExistingAddressId = addressesRepository.findMaxAddressId() + 1;
        Address sampleAddress = Address.builder().id(nonExistingAddressId).country("portugal").city("porto").street("rua das flores").number(123).build();
        sampleUser1 = User.builder().id(1L).name("marlao").email("mixooo@gmail.com").address(sampleAddress).password("1pow").build();
        User sampleUser2 = User.builder().id(9L).name("mima").password("youwww").build();
        User sampleUser3 = User.builder().id(3L).name("weiza").password("UAUUU").build();
        sampleUsers = List.of(sampleUser1, sampleUser2, sampleUser3);
        userToCreate = User.builder().name("fabio").password("123").build();

        sampleUsers = List.of(
                User.builder().id(5L).name("rita").password("LOOGOOO").build(),
                User.builder().id(6L).name("joao").password("rfierrs").build(),
                User.builder().id(7L).name("mara").password("fsdsa").build()
        );

        existingUser = User.builder().id(1L).name("oldName").password("oldPass").build();

        nonExistingUserId = usersRepository.findMaxUserId()+1;
        userWithNullFields = User.builder().id(98L).name("ricas").email("ohsoxa@gmail.com").address(null).password("123").build();
    }

    @Test
    void createUser_ReturnCreated() throws Exception {
        userService.createUser(sampleUser1);

        ResultActions response = mockMvc.perform(post("/user") //post method meaning it's a post request with the path to the method
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleUser1))); //user sample

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(sampleUser1.getId().intValue())))
                .andExpect(jsonPath("$.name", CoreMatchers.is(sampleUser1.getName())))
                .andExpect(jsonPath("$.password", CoreMatchers.is(sampleUser1.getPassword())));
    }

    @Test
    void getAllUsers_ReturnExpectedUsers() throws Exception{
        userService.getAllUsers();

        ResultActions response = mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleUsers)));

        response.andExpect(status().isOk());
    }

    @Test
    void getUserById_ReturnUser() throws Exception {
        userService.createUser(sampleUser1);
        Long returnedUserId = sampleUser1.getId();
        User returnedUser = userService.getUserById(returnedUserId);

        ResultActions response = mockMvc.perform(get("/user/{userID}", returnedUserId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id",CoreMatchers.is(returnedUser.getId().intValue())))
                .andExpect(jsonPath("$.name",CoreMatchers.is(returnedUser.getName())))
                .andExpect(jsonPath("$.password",CoreMatchers.is(returnedUser.getPassword())));
    }

    @Test
    void updateUser_ReturnUpdatedUser() throws Exception{

        User newUser = userService.createUser(sampleUser1);

        newUser.setName("bixoooo");
        newUser.setPassword("credoooo");

        User updatedUser = userService.updateUser(newUser);

        ResultActions response = mockMvc.perform(put("/user/{id}", newUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id",CoreMatchers.is(updatedUser.getId().intValue())))
                .andExpect(jsonPath("$.name", CoreMatchers.is(updatedUser.getName())))
                .andExpect(jsonPath("$.password",CoreMatchers.is(updatedUser.getPassword())));
    }

    @Test
    void updateUser_ThrowsUserFieldsCannotBeNullOrEmptyException() throws Exception {
        // Test multiple null fields
        performUpdateUserAndExpectBadRequest(userWithNullFields);

        // Test null name
        sampleUser1.setName(null);
        performUpdateUserAndExpectBadRequest(sampleUser1);

        // Test empty name
        sampleUser1.setName("");
        performUpdateUserAndExpectBadRequest(sampleUser1);

        // Test null password
        sampleUser1.setName("marlao");
        sampleUser1.setPassword(null);
        performUpdateUserAndExpectBadRequest(sampleUser1);

        // Test empty password
        sampleUser1.setPassword("");
        performUpdateUserAndExpectBadRequest(sampleUser1);

        // Test null address
        sampleUser1.setPassword("1pow");
        sampleUser1.setAddress(null);
        performUpdateUserAndExpectBadRequest(sampleUser1);
    }

    private void performUpdateUserAndExpectBadRequest(User user) throws Exception {
        mockMvc.perform(put("/user/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User fields cannot be null or empty"));
    }
    @Test
    void updateUser_ThrowsUserDoesNotMatchException() throws Exception{
        mockMvc.perform(put("/user/{userId}",2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("UserId and request body id do not match"));
    }

    @Test
    void deleteUser_ReturnDeletedUser() throws Exception{
        userService.createUser(sampleUser1);
        Long sampleUser1Id = sampleUser1.getId();


        ResultActions response = mockMvc.perform(delete("/user/{id}",sampleUser1Id)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
    }
}
