package com.mindera.fabio.usersdemo.integration_tests.repository;

import com.mindera.fabio.usersdemo.exceptions.UserNotFoundException;
import com.mindera.fabio.usersdemo.model.User;
import com.mindera.fabio.usersdemo.interfaces.UsersRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class UsersRepositoryTests {

    @Autowired
    private UsersRepository usersRepository;

    private User sample1;
    private User sample2;
    private List<User> usersSample;
    private Long maxUserId;

    @BeforeEach
    void init(){
        maxUserId = usersRepository.findMaxUserId();
        sample1 = User.builder().id(maxUserId+1).name("poxa").password("poqa").build();
        sample2 = User.builder().id(maxUserId+2).name("PIU").password("ppla").build();
        usersSample = List.of(sample1,sample2);
    }

    @Test
    void saveUser() {
        User savedUser = usersRepository.save(sample1);
        assertNotNull(savedUser.getId());
    }

    @Test
    void findUserById() {
        User savedUser = usersRepository.save(sample1);

        User foundUser = usersRepository.findById(savedUser.getId()).orElseThrow(UserNotFoundException::new);

        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals(savedUser.getName(), foundUser.getName());
        assertEquals(savedUser.getPassword(), foundUser.getPassword());
    }

    @Test
    void findAllUsers() {
        Long allUsersCount = usersRepository.countAllUsers();
        usersRepository.saveAll(usersSample);

        List<User> allUsers = usersRepository.findAll();

        assertEquals(usersSample.size(), allUsers.size()-allUsersCount);
    }

    @Test
    void userExistsById() {
        User savedUser = usersRepository.save(sample1);

        assertTrue(usersRepository.existsById(savedUser.getId()));
        assertFalse(usersRepository.existsById(maxUserId+1));
    }

    @Test
    void deleteUser() {
        User savedUser = usersRepository.save(sample1);

        usersRepository.delete(savedUser);

        assertFalse(usersRepository.existsById(savedUser.getId()));
    }

    @Test
    void findMaxUserId() {
        Long maxUserId = usersRepository.findMaxUserId();
        assertNotNull(maxUserId);
    }
}


