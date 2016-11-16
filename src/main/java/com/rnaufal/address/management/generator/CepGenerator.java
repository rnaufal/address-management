package com.rnaufal.address.management.generator;

import java.util.Optional;

/**
 * Created by rnaufal on 15/11/16.
 */
public interface CepGenerator {

    Optional<String> nextAvailable();
}
