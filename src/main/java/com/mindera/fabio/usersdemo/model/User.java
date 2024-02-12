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
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    private String password;

    @Override
    public boolean equals(Object user) {
        if(user== null || getClass() != user.getClass()){
            return false;
        }
        User convertedUser = (User) user;
        return Objects.equals(id, convertedUser.id) &&
                Objects.equals(name, convertedUser.name) &&
                Objects.equals(email, convertedUser.email) &&
                Objects.equals(address, convertedUser.address) &&
                Objects.equals(password, convertedUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, address, password);
    }
}
