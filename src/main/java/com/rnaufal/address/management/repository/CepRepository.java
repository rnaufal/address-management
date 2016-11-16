package com.rnaufal.address.management.repository;

import com.rnaufal.address.management.domain.Cep;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by rnaufal on 15/11/16.
 */
public interface CepRepository extends CrudRepository<Cep, Long> {

    Optional<Cep> findByValue(String cep);
}
