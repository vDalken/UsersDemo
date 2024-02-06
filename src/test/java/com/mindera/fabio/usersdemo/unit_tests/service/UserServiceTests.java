package com.mindera.fabio.usersdemo.unit_tests.service;

import com.mindera.fabio.usersdemo.exceptions.UserNotFoundException;
import com.mindera.fabio.usersdemo.interfaces.UsersRepository;
import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock //annonation used to mock an object, @MockBean is for integration tests
    private UsersRepository usersRepository;

    @InjectMocks //injecting the mock repository in the UserService
    private UserService userService; //the same as: UserService userService = new UserService(usersRepository);

    private User createdUser;

    private User userToCreate;
    private User sampleUser;
    private List<User> sampleUsers;
    private User existingUser;
    private User updatedUser;
    private User nonExistingUser;

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
    }

    @Test
    void createUser_ReturnsCreatedUser() {
        given(userService.createUser(userToCreate)).willReturn(createdUser);

        User result = userService.createUser(userToCreate);

        verify(usersRepository).save(userToCreate);
    }

    @Test
    void getUserById_ExistingUserId_ReturnsUser() {
        Long sampleUserId = sampleUser.getId();
        given(usersRepository.findById(sampleUserId)).willReturn(Optional.of(sampleUser));

        User result = userService.getUserById(sampleUserId);

        assertEquals(sampleUser, result);
    }

    @Test
    void getUserById_NonExistingUserId_ThrowUserNotFoundException() {
        long nonExistingUserId = 999L;

        given(usersRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(nonExistingUserId));
    }

    @Test
    void getAllUsers_ReturnAllUsers() {

        given(usersRepository.findAll()).willReturn(sampleUsers);

        List<User> result = userService.getAllUsers();

        assertEquals(sampleUsers, result);
    }

    @Test
    void updateUser_ExistingUserId_ReturnUpdatedUser() {
        given(usersRepository.existsById(existingUser.getId())).willReturn(true);

        given(usersRepository.save(updatedUser)).willReturn(updatedUser);

        User result = userService.updateUser(updatedUser);

        assertEquals(updatedUser, result);
    }

    @Test
    void updateUser_NonExistingUserId_ThrowUserNotFoundException() {
        given(usersRepository.existsById(nonExistingUser.getId())).willReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(nonExistingUser));
    }

    @Test
    void deleteUser_ExistingUserId_ReturnDeletedUser() {
        Long existingUserId = existingUser.getId();
        given(usersRepository.findById(existingUserId)).willReturn(Optional.of(existingUser));

        User result = userService.deleteUser(existingUserId);

        verify(usersRepository).delete(existingUser);

        assertEquals(existingUser, result);
    }

    @Test
    void deleteUser_NonExistingUserId_ThrowUserNotFoundException() {
        long nonExistingUserId = nonExistingUser.getId();
        given(usersRepository.findById(nonExistingUserId)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(nonExistingUserId));
    }
}
