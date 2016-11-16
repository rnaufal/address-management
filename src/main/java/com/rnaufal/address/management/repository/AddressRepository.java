package com.rnaufal.address.management.repository;

import com.rnaufal.address.management.domain.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by rnaufal on 14/11/16.
 */
public interface AddressRepository extends CrudRepository<Address, Long> {

    @Query("SELECT a " +
            "FROM Address a " +
            "JOIN FETCH a.cep " +
            "WHERE a.id = ?1")
    Optional<Address> findById(Long id);

    @Query("SELECT a " +
            "FROM Address a " +
            "JOIN FETCH a.cep " +
            "WHERE a.cep.value = ?1 " +
            "AND a.complement = ?2 " +
            "AND a.number = ?3")
    Optional<Address> findByCepAndComplementAndNumber(String cep,
                                                      String complement,
                                                      String number);
}
