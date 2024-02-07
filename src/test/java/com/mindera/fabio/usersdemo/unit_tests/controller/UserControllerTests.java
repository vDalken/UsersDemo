package com.mindera.fabio.usersdemo.unit_tests.controller;

import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.restcontrollers.UserController;
import com.mindera.fabio.usersdemo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class UserControllerTests {

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
        MockitoAnnotations.initMocks(this);

        sampleUser1 = User.builder().id(1L).name("marlao").password("1pow").build();
        sampleUser2 = User.builder().id(2L).name("mima").password("youwww").build();
        sampleUser3 = User.builder().id(3L).name("weiza").password("UAUUU").build();
        sampleUsers = List.of(sampleUser1, sampleUser2, sampleUser3);
    }

    @Test
    void getUserById_ReturnUser() {
        given(userService.getUserById(ArgumentMatchers.anyLong())).willReturn(sampleUser1);

        User returnedUser = userController.getUserById(1L);

        assertEquals(sampleUser1, returnedUser);
        verify(userService).getUserById(1L);
    }

    @Test
    void createUser_ReturnCreated() {
        given(userService.createUser(any(User.class))).willReturn(sampleUser1);

        User createdUser = userController.createUser(sampleUser1);

        assertEquals(sampleUser1, createdUser);
        verify(userService).createUser(sampleUser1);
    }

    @Test
    void getAllUsers_ReturnExpectedUsers() {
        given(userService.getAllUsers()).willReturn(sampleUsers);

        List<User> returnedUsers = userController.getAllUsers();

        assertEquals(sampleUsers, returnedUsers);
        verify(userService).getAllUsers();
    }

    @Test
    void updateUser_ReturnUpdatedUser() {
        given(userService.updateUser(any(User.class))).willReturn(sampleUser2);

        User updatedUser = userController.updateUser(sampleUser2.getId(),sampleUser2);

        assertEquals(sampleUser2, updatedUser);
        verify(userService).updateUser(sampleUser2);
    }

    @Test
    void deleteUser_ReturnDeletedUser() {
        given(userService.deleteUser(ArgumentMatchers.anyLong())).willReturn(sampleUser1);

        User deletedUser = userController.deleteUser(1L);

        assertEquals(sampleUser1, deletedUser);
        verify(userService).deleteUser(1L);
    }
}
