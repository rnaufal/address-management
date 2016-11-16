package com.rnaufal.address.management.service;

import com.rnaufal.address.management.domain.Cep;
import com.rnaufal.address.management.exception.CepNotFoundException;
import com.rnaufal.address.management.generator.CepGenerator;
import com.rnaufal.address.management.generator.ReplaceRightDigitToZeroCepGenerator;
import com.rnaufal.address.management.repository.CepRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by rnaufal on 15/11/16.
 */
@Service
public class CepSearchServiceImpl implements CepSearchService {

    private final CepRepository cepRepository;

    @Autowired
    public CepSearchServiceImpl(CepRepository cepRepository) {
        this.cepRepository = cepRepository;
    }

    @Override
    public Cep findBy(String cep) {

        checkArgument(StringUtils.isNotBlank(cep), "cep cannot be null");

        Optional<Cep> candidateCep = cepRepository.findByValue(cep);
        CepGenerator cepGenerator = new ReplaceRightDigitToZeroCepGenerator(cep);

        while (!candidateCep.isPresent()) {
            candidateCep = cepGenerator
                    .nextAvailable()
                    .map(cepRepository::findByValue)
                    .orElseThrow(() -> new CepNotFoundException(cep));
        }

        return candidateCep.get();
    }
}
