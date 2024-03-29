package com.mindera.fabio.usersdemo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    private String city;
    private String street;
    private Integer number;

    @Override
    public boolean equals(Object address) {
        if(address == null || getClass() != address.getClass()){
            return false;
        }
        Address convertedAddress = (Address) address;
        return Objects.equals(id, convertedAddress.id) &&
                Objects.equals(country, convertedAddress.country) &&
                Objects.equals(city, convertedAddress.city) &&
                Objects.equals(street, convertedAddress.street) &&
                Objects.equals(number, convertedAddress.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, city, street, number);
    }
}
