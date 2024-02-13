package com.mindera.fabio.usersdemo.interfaces;

import com.mindera.fabio.usersdemo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressesRepository extends JpaRepository<Address, Long> {
    @Query(value = "SELECT MAX(id) FROM address", nativeQuery = true)
    Long findMaxAddressId();
}
