package com.rnaufal.address.management.generator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by rnaufal on 15/11/16.
 */
public class ReplaceRightDigitToZeroCepGenerator implements CepGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplaceRightDigitToZeroCepGenerator.class);

    private final StringBuilder cep;

    private int currentIndex;

    public ReplaceRightDigitToZeroCepGenerator(final String cep) {
        checkArgument(StringUtils.isNotBlank(cep), "cep cannot be null");

        this.cep = new StringBuilder(cep);
        this.currentIndex = cep.length();
    }

    @Override
    public Optional<String> nextAvailable() {
        if (--currentIndex < 0) {
            return Optional.empty();
        }

        this.cep.setCharAt(currentIndex, '0');
        String newCep = this.cep.toString();

        LOGGER.info("Cep {} was generated", newCep);

        return Optional.of(newCep);
    }
}
