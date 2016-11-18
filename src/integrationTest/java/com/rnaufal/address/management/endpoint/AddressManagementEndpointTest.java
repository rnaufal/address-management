package com.rnaufal.address.management.endpoint;

import com.rnaufal.address.management.domain.Address;
import com.rnaufal.address.management.domain.Cep;
import com.rnaufal.address.management.exception.AddressNotFoundException;
import com.rnaufal.address.management.exception.CepNotFoundException;
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

import static com.rnaufal.address.management.endpoint.CepFactory.createCep;
import static com.rnaufal.address.management.endpoint.ObjectToJsonFactory.toJson;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
    public void shouldCreateAddressWithSuccess() throws Exception {
        Address address = buildAddress();

        doReturn(Optional.empty()).when(addressManagementService).findByCriteria(address);
        doReturn(address).when(addressManagementService).createAddress(address);

        Cep cep = address.getCep();

        mockMvc.perform(post("/address")
                .content(toJson(address))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
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
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.cep.value").value(is(equalTo(cep.getValue()))))
                .andExpect(jsonPath("$.cep.city").value(is(equalTo(cep.getCity()))))
                .andExpect(jsonPath("$.cep.district").value(is(equalTo(cep.getDistrict()))))
                .andExpect(jsonPath("$.cep.estate").value(is(equalTo(cep.getEstate()))))
                .andExpect(jsonPath("$.cep.street").value(is(equalTo(cep.getStreet()))))
                .andExpect(jsonPath("$.complement").value(is(equalTo(address.getComplement()))))
                .andExpect(jsonPath("$.number").value(is(equalTo(address.getNumber()))));
    }

    @Test
    public void shouldUpdateAddressWithSuccess() throws Exception {
        Address address = buildAddress();
        address.setId(15L);
        address.setNumber("45");
        address.setComplement("Next to the grocery");
        address.getCep().setValue("87654321");
        address.getCep().setCity("Other beautiful city");

        doReturn(address).when(addressManagementService).updateAddress(address, address.getId());

        Cep cep = address.getCep();

        mockMvc.perform(post("/address/{id}", address.getId())
                .content(toJson(address))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.cep.value").value(is(equalTo(cep.getValue()))))
                .andExpect(jsonPath("$.cep.city").value(is(equalTo(cep.getCity()))))
                .andExpect(jsonPath("$.cep.district").value(is(equalTo(cep.getDistrict()))))
                .andExpect(jsonPath("$.cep.estate").value(is(equalTo(cep.getEstate()))))
                .andExpect(jsonPath("$.cep.street").value(is(equalTo(cep.getStreet()))))
                .andExpect(jsonPath("$.complement").value(is(equalTo(address.getComplement()))))
                .andExpect(jsonPath("$.number").value(is(equalTo(address.getNumber()))));
    }

    @Test
    public void shouldReturnBadRequestWhenTryingToUpdateAddressNotFound() throws Exception {
        Address address = buildAddress();
        address.setId(25L);

        doThrow(AddressNotFoundException.class)
                .when(addressManagementService)
                .updateAddress(address, address.getId());

        mockMvc.perform(post("/address/{id}", address.getId())
                .content(toJson(address))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void shouldReturnBadRequestWhenCepNotFound() throws Exception {
        Address address = buildAddress();
        address.setId(10L);

        doThrow(CepNotFoundException.class)
                .when(addressManagementService)
                .updateAddress(address, address.getId());

        mockMvc.perform(post("/address/{id}", address.getId())
                .content(toJson(address))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void shouldReturnBadRequestWhenAddressNotFound() throws Exception {
        long id = 10L;

        doThrow(AddressNotFoundException.class)
                .when(addressManagementService)
                .findById(id);

        mockMvc.perform(get("/address/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFindAddressWithSuccess() throws Exception {
        Address address = buildAddress();
        long id = 20L;

        doReturn(address)
                .when(addressManagementService)
                .findById(id);

        Cep cep = address.getCep();

        mockMvc.perform(get("/address/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.cep.value").value(is(equalTo(cep.getValue()))))
                .andExpect(jsonPath("$.cep.city").value(is(equalTo(cep.getCity()))))
                .andExpect(jsonPath("$.cep.district").value(is(equalTo(cep.getDistrict()))))
                .andExpect(jsonPath("$.cep.estate").value(is(equalTo(cep.getEstate()))))
                .andExpect(jsonPath("$.cep.street").value(is(equalTo(cep.getStreet()))))
                .andExpect(jsonPath("$.complement").value(is(equalTo(address.getComplement()))))
                .andExpect(jsonPath("$.number").value(is(equalTo(address.getNumber()))));
    }

    @Test
    public void shouldReturnBadRequestWhenTryingToDeleteAddressNotFound() throws Exception {
        long id = 10L;

        doThrow(AddressNotFoundException.class)
                .when(addressManagementService)
                .delete(id);

        mockMvc.perform(delete("/address/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldDeleteAddressWithSuccess() throws Exception {
        Address address = buildAddress();
        address.setId(30L);

        doReturn(address)
                .when(addressManagementService)
                .delete(address.getId());

        Cep cep = address.getCep();

        mockMvc.perform(delete("/address/{id}", address.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.cep.value").value(is(equalTo(cep.getValue()))))
                .andExpect(jsonPath("$.cep.city").value(is(equalTo(cep.getCity()))))
                .andExpect(jsonPath("$.cep.district").value(is(equalTo(cep.getDistrict()))))
                .andExpect(jsonPath("$.cep.estate").value(is(equalTo(cep.getEstate()))))
                .andExpect(jsonPath("$.cep.street").value(is(equalTo(cep.getStreet()))))
                .andExpect(jsonPath("$.complement").value(is(equalTo(address.getComplement()))))
                .andExpect(jsonPath("$.number").value(is(equalTo(address.getNumber()))));
    }

    private Address buildAddress() {
        Cep cep = createCep();
        Address address = new Address();
        address.setCep(cep);
        address.setComplement("<<COMPLEMENT>>");
        address.setNumber("45");
        return address;
    }
}
