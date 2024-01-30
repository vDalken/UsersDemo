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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    private User sampleUser;

    //before each annotation refreshes the state of every resource before initializing another test
    @BeforeEach
    public void init() {
        sampleUser = User.builder().id(1L).name("marlao").password("1pow").build();
    }

    @Test
    public void UserController_createUser_ReturnCreated() throws Exception {
        //given method allows me to define the method to be called and the arguments to be passed
        given(userService.createUser(ArgumentMatchers.any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
        //willAnswer defines what should be returned by the method call and says it should be it's own argument

        //response will store the info about the execution of the mock HTTP method
        ResultActions response = mockMvc.perform(post("/user") //post method meaning it's a post request with the path to the method
                .contentType(MediaType.APPLICATION_JSON) //says the content type of the request is a JSON
                .content(objectMapper.writeValueAsString(sampleUser))); //user sample

        //defining what status code to expect from the response of the HTTP method
        //in this case we're expecting 200
        response.andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(sampleUser.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(sampleUser.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(sampleUser.getPassword())));
    }


}
