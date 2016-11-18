package com.rnaufal.address.management.endpoint;

import com.rnaufal.address.management.domain.Cep;

/**
 * Created by rnaufal on 18/11/16.
 */
class CepFactory {

    private CepFactory() {

    }

    static Cep createCep() {
        Cep cep = new Cep();
        cep.setId(10L);
        cep.setValue("12345678");
        cep.setCity("<<CITY>>");
        cep.setDistrict("<<DISTRICT>>");
        cep.setEstate("<<ESTATE>>");
        cep.setStreet("<<STREET>>");
        return cep;
    }
}
