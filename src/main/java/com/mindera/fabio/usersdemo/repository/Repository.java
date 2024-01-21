package com.mindera.fabio.usersdemo.repository;

import com.mindera.fabio.usersdemo.interfaces.UsersRepository;
import com.mindera.fabio.usersdemo.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Component
@RequiredArgsConstructor
public class Repository implements UsersRepository {

    private final List<User> users;

    public User createUser(final User user) {
        users.add(user);
        return user;
    }

    public User getUserById(final Long id) {
        return users
                .stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public User updateUser(final User user) {
        users
                .stream()
                .filter(listUser -> Objects.equals(listUser.getId(), user.getId()))
                .findFirst()
                .ifPresent(listUser -> {
                    listUser.setName(user.getName());
                    listUser.setPassword(user.getPassword());
                });
        return user;
    }

    public User deleteUser(final Long id) {
        Optional<User> userToBeDeleted = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        users.removeIf(user -> user.getId().equals(id));

        return userToBeDeleted.orElse(null);
    }
}
