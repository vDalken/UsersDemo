package com.mindera.fabio.usersdemo.interfaces;

import com.mindera.fabio.usersdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
}
