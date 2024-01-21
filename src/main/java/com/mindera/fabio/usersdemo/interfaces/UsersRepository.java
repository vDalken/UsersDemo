package com.mindera.fabio.usersdemo.interfaces;

import com.mindera.fabio.usersdemo.model.User;

public interface UsersRepository {
    public User createUser(final User user);

    public User getUserById(final Long id);

    public User updateUser(final User user);

    public User deleteUser(final Long id);
}
