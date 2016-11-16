package com.rnaufal.address.management.endpoint;

import com.rnaufal.address.management.domain.Address;
import com.rnaufal.address.management.service.AddressManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

/**
 * Created by rnaufal on 14/11/16.
 */
@RestController
public class AddressManagementEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressManagementEndpoint.class);

    private final AddressManagementService addressManagementService;

    @Autowired
    public AddressManagementEndpoint(final AddressManagementService addressManagementService) {
        this.addressManagementService = addressManagementService;
    }

    @RequestMapping(value = "/address",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createAddress(@Validated @RequestBody final Address addressRequest) {
        LOGGER.info("{} received", addressRequest);

        Optional<Address> optionalAddress = addressManagementService.findByCriteria(addressRequest);

        if (optionalAddress.isPresent()) {
            return ResponseEntity.ok(optionalAddress.get());
        } else {
            Address address = addressManagementService.createAddress(addressRequest);
            return ResponseEntity.created(uriFrom(address)).body(address);
        }
    }

    @RequestMapping(value = "/address/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateAddress(@PathVariable final Long id,
                                           @Validated @RequestBody final Address addressRequest) {
        LOGGER.info("{} received", addressRequest);

        Address address = addressManagementService.updateAddress(addressRequest, id);
        return ResponseEntity.ok(address);
    }

    @RequestMapping(value = "/address/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> findAddress(@PathVariable final Long id) {
        LOGGER.info("Address id = {} received", id);

        Address address = addressManagementService.findById(id);
        return ResponseEntity.ok(address);
    }

    @RequestMapping(value = "/address/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        LOGGER.info("Address id = {} delete request received", id);

        Address deletedAddress = addressManagementService.delete(id);

        return ResponseEntity.ok(deletedAddress);
    }

    private URI uriFrom(Address address) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(address.getId()).toUri();
    }
}
