package com.rnaufal.address.management.service;

import com.rnaufal.address.management.domain.Cep;

/**
 * Created by rnaufal on 15/11/16.
 */
public interface CepSearchService {

    Cep findBy(String cep);
}
