package com.rnaufal.address.management.service;

import com.rnaufal.address.management.domain.Address;
import com.rnaufal.address.management.domain.Cep;
import com.rnaufal.address.management.exception.AddressNotFoundException;
import com.rnaufal.address.management.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by rnaufal on 14/11/16.
 */
@Service
public class AddressManagementServiceImpl implements AddressManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressManagementServiceImpl.class);

    private final CepSearchService cepSearchService;

    private final AddressRepository addressRepository;

    @Autowired
    public AddressManagementServiceImpl(final CepSearchService cepSearchService,
                                        final AddressRepository addressRepository) {
        this.cepSearchService = cepSearchService;
        this.addressRepository = addressRepository;
    }

    @Override
    public Address createAddress(final Address address) {
        checkArgument(address != null, "address cannot be null");

        LOGGER.info("Creating address: {}", address);

        Cep cep = findCep(address);

        address.setCep(cep);

        this.addressRepository.save(address);
        return address;
    }

    @Override
    public Address findById(final Long id) {
        checkArgument(id != null, "address cannot be null");

        return addressRepository
                .findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id));
    }

    @Override
    public Optional<Address> findByCriteria(final Address address) {
        checkArgument(address != null, "address cannot be null");

        LOGGER.info("Finding address: {}", address);

        return addressRepository.findByCepAndComplementAndNumber(address.getCep().getValue(),
                address.getComplement(),
                address.getNumber());
    }

    @Override
    public Address delete(Long id) {
        checkArgument(id != null, "id cannot be null");

        Address address = findById(id);

        addressRepository.delete(address);

        return address;
    }

    @Override
    public Address updateAddress(Address address, Long id) {
        checkArgument(address != null, "address cannot be null");
        checkArgument(id != null, "id cannot be null");

        Cep cep = findCep(address);

        Address addressToUpdate = findById(id);
        addressToUpdate.setCep(cep);
        addressToUpdate.setComplement(address.getComplement());
        addressToUpdate.setNumber(address.getNumber());

        addressRepository.save(addressToUpdate);

        return addressToUpdate;
    }

    private Cep findCep(Address address) {
        return cepSearchService.findBy(address.getCep().getValue());
    }
}
