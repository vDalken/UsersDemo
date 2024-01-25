package com.mindera.fabio.usersdemo.interfaces;

import com.mindera.fabio.usersdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//declares a JPA repository that is going to handle users entities
//a JPA repository should be given to each entity
@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
}
