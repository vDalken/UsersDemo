package com.mindera.fabio.usersdemo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;

    @Override
    public boolean equals(Object user) {
        if(getClass() != user.getClass()){
            return false;
        }
        User convertedUser = (User) user;
        return Objects.equals(id, convertedUser.id) &&
                Objects.equals(name, convertedUser.name) &&
                Objects.equals(password, convertedUser.password);
    }
}
