package com.rnaufal.address.management.service;

import com.rnaufal.address.management.domain.Address;

import java.util.Optional;

/**
 * Created by rnaufal on 14/11/16.
 */
public interface AddressManagementService {
    
    Address createAddress(Address address);

    Address findById(Long id);

    Address delete(Long id);

    Address updateAddress(Address address, Long id);

    Optional<Address> findByCriteria(Address address);
}
