package com.mindera.fabio.usersdemo.integration_tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.restcontrollers.UserController;
import com.mindera.fabio.usersdemo.services.UserService;
import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Transactional
@SpringBootTest
public class UserControllerTests {

    private MockMvc mockMvc; //to mock http requests

    @Autowired
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
        sampleUser2 = User.builder().id(9L).name("mima").password("youwww").build();
        sampleUser3 = User.builder().id(3L).name("weiza").password("UAUUU").build();
        sampleUsers = List.of(sampleUser1,sampleUser2,sampleUser3);
        mockMvc =  MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @Test
    void createUser_ReturnCreated() throws Exception {
        //given method allows me to define the method to be called and the arguments to be passed
        userService.createUser(sampleUser1);

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
    void getAllUsers_ReturnExpectedUsers() throws Exception{
        //created a sample list of users to mock the return content of the method call
        //since it doesn't take any arguments so I don't know what to expect besides a list of objects of type User
        userService.getAllUsers();

        //mocked the get http request, and sets the content type of the method as JSON and says that
        //the content should be a JSON string of all the objects it got from the mocked service method call
        ResultActions response = mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleUsers)));

        //checking if the status of the response is 200, which means ok
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getUser_ReturnUser() throws Exception {
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
    void deleteUser_ReturnDeletedUser() throws Exception{
        userService.createUser(sampleUser1);
        Long sampleUser1Id = sampleUser1.getId();


        ResultActions response = mockMvc.perform(delete("/user/{id}",sampleUser1Id)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
