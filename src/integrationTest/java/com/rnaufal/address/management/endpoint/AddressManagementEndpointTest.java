package com.rnaufal.address.management.endpoint;

import com.rnaufal.address.management.domain.Address;
import com.rnaufal.address.management.domain.Cep;
import com.rnaufal.address.management.service.AddressManagementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static com.rnaufal.address.management.endpoint.ObjectToJsonFactory.toJson;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by rnaufal on 16/11/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressManagementEndpointTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AddressManagementService addressManagementService;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createAddressWithSuccess() throws Exception {
        Address address = buildAddress();

        doReturn(Optional.empty()).when(addressManagementService).findByCriteria(address);
        doReturn(address).when(addressManagementService).createAddress(address);

        Cep cep = address.getCep();

        mockMvc.perform(post("/address")
                .content(toJson(address))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cep.value").value(is(equalTo(cep.getValue()))))
                .andExpect(jsonPath("$.cep.city").value(is(equalTo(cep.getCity()))))
                .andExpect(jsonPath("$.cep.district").value(is(equalTo(cep.getDistrict()))))
                .andExpect(jsonPath("$.cep.estate").value(is(equalTo(cep.getEstate()))))
                .andExpect(jsonPath("$.cep.street").value(is(equalTo(cep.getStreet()))))
                .andExpect(jsonPath("$.complement").value(is(equalTo(address.getComplement()))))
                .andExpect(jsonPath("$.number").value(is(equalTo(address.getNumber()))));
    }

    @Test
    public void shouldReturnBadRequestWhenRequiredFieldsAreNotPresent() throws Exception {
        Address address = buildAddress();
        address.setNumber(null);

        mockMvc.perform(post("/address")
                .content(toJson(address))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnAlreadyCreatedAddress() throws Exception {
        Address address = buildAddress();

        doReturn(Optional.of(address)).when(addressManagementService).findByCriteria(address);

        Cep cep = address.getCep();

        mockMvc.perform(post("/address")
                .content(toJson(address))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep.value").value(is(equalTo(cep.getValue()))))
                .andExpect(jsonPath("$.cep.city").value(is(equalTo(cep.getCity()))))
                .andExpect(jsonPath("$.cep.district").value(is(equalTo(cep.getDistrict()))))
                .andExpect(jsonPath("$.cep.estate").value(is(equalTo(cep.getEstate()))))
                .andExpect(jsonPath("$.cep.street").value(is(equalTo(cep.getStreet()))))
                .andExpect(jsonPath("$.complement").value(is(equalTo(address.getComplement()))))
                .andExpect(jsonPath("$.number").value(is(equalTo(address.getNumber()))));
    }

    private Address buildAddress() {
        Cep cep = buildCep();
        Address address = new Address();
        address.setCep(cep);
        address.setComplement("<<COMPLEMENT>>");
        address.setNumber("45");
        return address;
    }

    private Cep buildCep() {
        Cep cep = new Cep();
        cep.setValue("45678912");
        cep.setCity("<<CITY>>");
        cep.setDistrict("<<DISTRICT>>");
        cep.setEstate("<<ESTATE>>");
        cep.setStreet("<<STREET>>");
        return cep;
    }
}
