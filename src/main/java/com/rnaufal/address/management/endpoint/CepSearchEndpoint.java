package com.rnaufal.address.management.endpoint;

import com.rnaufal.address.management.domain.Cep;
import com.rnaufal.address.management.domain.validator.CEP;
import com.rnaufal.address.management.service.CepSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rnaufal on 14/11/16.
 */
@RestController
@Validated
public class CepSearchEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(CepSearchEndpoint.class);

    private final CepSearchService cepSearchService;

    @Autowired
    public CepSearchEndpoint(final CepSearchService cepSearchService) {
        this.cepSearchService = cepSearchService;
    }

    @RequestMapping(path = "/cep/{cep}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> findAddressBy(@CEP
                                           @PathVariable("cep") final String searchCep) {
        LOGGER.info("Request received: {}", searchCep);

        Cep cep = cepSearchService.findBy(searchCep);
        return ResponseEntity.ok(cep);
    }
}
