package com.rnaufal.address.management.endpoint;

import com.rnaufal.address.management.domain.Cep;
import com.rnaufal.address.management.exception.CepNotFoundException;
import com.rnaufal.address.management.service.CepSearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.rnaufal.address.management.endpoint.CepFactory.createCep;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by rnaufal on 16/11/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CepSearchEndpointTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CepSearchService cepSearchService;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldFindCepWithSuccess() throws Exception {
        Cep cep = createCep();

        doReturn(cep).when(cepSearchService).findBy(cep.getValue());

        mockMvc.perform(get("/cep/{cep}", cep.getValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(equalTo(10))))
                .andExpect(jsonPath("$.value").value(is(equalTo(cep.getValue()))))
                .andExpect(jsonPath("$.city").value(is(equalTo(cep.getCity()))))
                .andExpect(jsonPath("$.district").value(is(equalTo(cep.getDistrict()))))
                .andExpect(jsonPath("$.estate").value(is(equalTo(cep.getEstate()))))
                .andExpect(jsonPath("$.street").value(is(equalTo(cep.getStreet()))));
    }

    @Test
    public void shouldReturnBadRequestWhenCepIsInvalid() throws Exception {
        mockMvc.perform(get("/cep/{cep}", "123abc45")).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenCepNoExists() throws Exception {
        String cep = "45678912";

        doThrow(CepNotFoundException.class).when(cepSearchService).findBy(cep);

        mockMvc.perform(get("/cep/{cep}", cep)).andExpect(status().isBadRequest());
    }
}
