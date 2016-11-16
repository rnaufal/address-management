package com.rnaufal.address.management.service;

import com.rnaufal.address.management.domain.Cep;
import com.rnaufal.address.management.exception.CepNotFoundException;
import com.rnaufal.address.management.repository.CepRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Created by rnaufal on 16/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class CepSearchServiceTest {

    @Mock
    private CepRepository cepRepository;

    private CepSearchService cepSearchService;

    @Before
    public void setUp() throws Exception {
        this.cepSearchService = new CepSearchServiceImpl(cepRepository);
    }

    @Test
    public void shouldFindCepWithSuccess() {
        Cep cep = new Cep();
        cep.setValue("12345678");

        doReturn(Optional.of(cep))
                .when(cepRepository)
                .findByValue(cep.getValue());

        Cep actual = cepSearchService.findBy(cep.getValue());
        assertThat(actual.getValue(), is(equalTo(cep.getValue())));
    }

    @Test
    public void shouldFindCepWithSuccessReplacingRightDigitToZero() {
        String expectedCep = "12345678";
        Cep cep = new Cep();
        cep.setValue("12340000");

        doReturn(Optional.empty()).when(cepRepository).findByValue(expectedCep);
        doReturn(Optional.empty()).when(cepRepository).findByValue("12345670");
        doReturn(Optional.empty()).when(cepRepository).findByValue("12345600");
        doReturn(Optional.empty()).when(cepRepository).findByValue("12345000");
        doReturn(Optional.of(cep)).when(cepRepository).findByValue(cep.getValue());

        Cep actual = cepSearchService.findBy(expectedCep);
        assertThat(actual.getValue(), is(equalTo(cep.getValue())));
    }

    @Test(expected = CepNotFoundException.class)
    public void shouldNotFindCepWithSuccess() {
        String cep = "45678934";

        doReturn(Optional.empty()).when(cepRepository).findByValue(cep);
        doReturn(Optional.empty()).when(cepRepository).findByValue("45678930");
        doReturn(Optional.empty()).when(cepRepository).findByValue("45678900");
        doReturn(Optional.empty()).when(cepRepository).findByValue("45678000");
        doReturn(Optional.empty()).when(cepRepository).findByValue("45670000");
        doReturn(Optional.empty()).when(cepRepository).findByValue("45600000");
        doReturn(Optional.empty()).when(cepRepository).findByValue("45000000");
        doReturn(Optional.empty()).when(cepRepository).findByValue("40000000");
        doReturn(Optional.empty()).when(cepRepository).findByValue("00000000");

        cepSearchService.findBy(cep);
    }
}