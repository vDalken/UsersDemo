package com.mindera.fabio.usersdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.restcontrollers.UserController;
import com.mindera.fabio.usersdemo.services.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc; //to mock http requests

    @MockBean
    private UserService userService; //to mock this method call

    @Autowired
    private ObjectMapper objectMapper; //to map my object to JSON format

    private User sampleUser1;
    private User sampleUser2;
    private User sampleUser3;
    List<User> sampleUsers;

    //before each annotation refreshes the state of every resource before initializing another test
    @BeforeEach
    public void init() {
        sampleUser1 = User.builder().id(1L).name("marlao").password("1pow").build();
        sampleUser2 = User.builder().id(2L).name("mima").password("youwww").build();
        sampleUser3 = User.builder().id(3L).name("weiza").password("UAUUU").build();
        sampleUsers = List.of(sampleUser1,sampleUser2,sampleUser3);
    }

    @Test
    public void UserController_createUser_ReturnCreated() throws Exception {
        //given method allows me to define the method to be called and the arguments to be passed
        given(userService.createUser(ArgumentMatchers.any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
        //willAnswer defines what should be returned by the method call and says it should be it's own argument

        //response will store the info about the execution of the mock HTTP method
        ResultActions response = mockMvc.perform(post("/user") //post method meaning it's a post request with the path to the method
                .contentType(MediaType.APPLICATION_JSON) //says the content type of the request is a JSON
                .content(objectMapper.writeValueAsString(sampleUser1))); //user sample

        //defining what status code to expect from the response of the HTTP method
        //in this case we're expecting 200
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(sampleUser1.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(sampleUser1.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(sampleUser1.getPassword())));
    }

    @Test
    public void UserController_getAllUsers_ReturnExpectedUsers() throws Exception{
        //created a sample list of users to mock the return content of the method call
        //since it doesn't take any arguments so I don't know what to expect besides a list of objects of type User
        given(userService.getAllUsers()).willReturn(sampleUsers);

        //mocked the get http request, and sets the content type of the method as JSON and says that
        //the content should be a JSON string of all the objects it got from the mocked service method call
        ResultActions response = mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleUsers)));

        //checking if the status of the response is 200, which means ok
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void UserController_getUser_ReturnUser() throws Exception {
        given(userService.getUserById(ArgumentMatchers.anyLong())).willReturn(sampleUser2);

        ResultActions response = mockMvc.perform(get("/user/{userID}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(sampleUser2)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",CoreMatchers.is(sampleUser2.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name",CoreMatchers.is(sampleUser2.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password",CoreMatchers.is(sampleUser2.getPassword())));
    }

    @Test
    public void UserController_updateUser_ReturnUpdatedUser() throws Exception{
        given(userService.updateUser(ArgumentMatchers.any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/user/{id}",2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleUser2)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",CoreMatchers.is(sampleUser2.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(sampleUser2.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password",CoreMatchers.is(sampleUser2.getPassword())));
    }

    @Test
    public void UserController_deleteUser_ReturnDeletedUser() throws Exception{
        given(userService.deleteUser(anyLong())).willAnswer(invocation -> sampleUser1);

        ResultActions response = mockMvc.perform(delete("/user/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleUser1)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",CoreMatchers.is(sampleUser1.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name",CoreMatchers.is(sampleUser1.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password",CoreMatchers.is(sampleUser1.getPassword())));
    }
}
