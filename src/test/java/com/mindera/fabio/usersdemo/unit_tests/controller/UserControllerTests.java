package com.mindera.fabio.usersdemo.unit_tests.controller;

import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.restcontrollers.UserController;
import com.mindera.fabio.usersdemo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User sampleUser1;
    private User sampleUser2;
    private User sampleUser3;
    private List<User> sampleUsers;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        sampleUser1 = User.builder().id(1L).name("marlao").password("1pow").build();
        sampleUser2 = User.builder().id(2L).name("mima").password("youwww").build();
        sampleUser3 = User.builder().id(3L).name("weiza").password("UAUUU").build();
        sampleUsers = List.of(sampleUser1, sampleUser2, sampleUser3);
    }

    @Test
    void getUserById_ReturnUser() throws Exception {
        given(userService.getUserById(ArgumentMatchers.anyLong())).willReturn(sampleUser1);

        mockMvc.perform(get("/user/{userID}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleUser1.getId().intValue()))
                .andExpect(jsonPath("$.name").value(sampleUser1.getName()))
                .andExpect(jsonPath("$.password").value(sampleUser1.getPassword()));
    }

    @Test
    void createUser_ReturnCreated() throws Exception {
        given(userService.createUser(any(User.class))).willReturn(sampleUser1);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"marlao\",\"password\":\"1pow\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleUser1.getId().intValue()))
                .andExpect(jsonPath("$.name").value(sampleUser1.getName()))
                .andExpect(jsonPath("$.password").value(sampleUser1.getPassword()));
    }

    @Test
    void getAllUsers_ReturnExpectedUsers() throws Exception {
        given(userService.getAllUsers()).willReturn(sampleUsers);

        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_ReturnUpdatedUser() throws Exception {
        given(userService.updateUser(any(User.class))).willReturn(sampleUser2);

        mockMvc.perform(put("/user/{userId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":2,\"name\":\"mima\",\"password\":\"youwww\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleUser2.getId().intValue()))
                .andExpect(jsonPath("$.name").value(sampleUser2.getName()))
                .andExpect(jsonPath("$.password").value(sampleUser2.getPassword()));
    }

    @Test
    void deleteUser_ReturnDeletedUser() throws Exception {
        given(userService.deleteUser(ArgumentMatchers.anyLong())).willReturn(sampleUser1);

        mockMvc.perform(delete("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleUser1.getId().intValue()))
                .andExpect(jsonPath("$.name").value(sampleUser1.getName()))
                .andExpect(jsonPath("$.password").value(sampleUser1.getPassword()));
    }
}