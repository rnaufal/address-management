package com.rnaufal.address.management.service;

import com.rnaufal.address.management.domain.Address;
import com.rnaufal.address.management.domain.Cep;
import com.rnaufal.address.management.exception.AddressNotFoundException;
import com.rnaufal.address.management.exception.CepNotFoundException;
import com.rnaufal.address.management.repository.AddressRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by rnaufal on 16/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddressManagementServiceTest {

    @Mock
    private CepSearchService cepSearchService;

    @Mock
    private AddressRepository addressRepository;

    private AddressManagementService addressManagementService;

    @Before
    public void setUp() throws Exception {
        this.addressManagementService = new AddressManagementServiceImpl(cepSearchService, addressRepository);
    }

    @Test
    public void shouldCreateAddressWithSuccess() {
        Address expected = buildAddress();

        doReturn(expected.getCep())
                .when(cepSearchService)
                .findBy(expected.getCep().getValue());

        Address actual = addressManagementService.createAddress(buildAddress());

        assertThat(actual, samePropertyValuesAs(expected));
        verify(addressRepository).save(actual);
    }

    @Test(expected = CepNotFoundException.class)
    public void shouldThrowExceptionWhenCepNotExists() {
        Address expected = buildAddress();

        doThrow(new CepNotFoundException(expected.getCep().getValue()))
                .when(cepSearchService)
                .findBy(expected.getCep().getValue());

        addressManagementService.createAddress(buildAddress());
    }

    @Test
    public void shouldFindAddressWithSuccess() {
        long id = 10L;
        Address expected = buildAddress();

        doReturn(Optional.of(expected))
                .when(addressRepository)
                .findById(id);

        Address actual = addressManagementService.findById(id);

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test(expected = AddressNotFoundException.class)
    public void shouldThrowExceptionWhenAddressNotFound() {
        long id = 15L;

        doThrow(new AddressNotFoundException(id))
                .when(addressRepository)
                .findById(id);

        addressManagementService.findById(id);
    }

    @Test
    public void shouldFindAddressBasedOnMultipleCriteria() {
        Address expected = buildAddress();

        doReturn(Optional.of(expected))
                .when(addressRepository)
                .findByCepAndComplementAndNumber(expected.getCep().getValue(),
                        expected.getComplement(),
                        expected.getNumber());

        Optional<Address> optionalAddress = addressManagementService.findByCriteria(expected);

        if (!optionalAddress.isPresent()) {
            fail("Address should be present");
        }

        assertThat(optionalAddress.get(), samePropertyValuesAs(expected));
    }

    @Test
    public void shouldNotFindAddressBasedOnMultipleCriteria() {
        Address expected = buildAddress();

        doReturn(Optional.empty())
                .when(addressRepository)
                .findByCepAndComplementAndNumber(expected.getCep().getValue(),
                        expected.getComplement(),
                        expected.getNumber());

        Optional<Address> optionalAddress = addressManagementService.findByCriteria(expected);

        assertThat(optionalAddress.isPresent(), is(equalTo(false)));
    }

    @Test
    public void shouldDeleteAddressWithSuccess() {
        long id = 20L;
        Address address = buildAddress();

        doReturn(Optional.of(address))
                .when(addressRepository)
                .findById(id);

        Address deleted = addressManagementService.delete(id);

        assertThat(deleted, samePropertyValuesAs(address));
        verify(addressRepository).delete(deleted);
    }

    @Test(expected = AddressNotFoundException.class)
    public void shouldThrowExceptionWhenAddressNotFoundToDelete() {
        long id = 20L;

        doThrow(new AddressNotFoundException(id))
                .when(addressRepository)
                .findById(id);

        addressManagementService.delete(id);
    }

    @Test
    public void shouldUpdateAddressWithSuccess() {
        long id = 10L;

        Address updatedAddress = buildAddress();
        updatedAddress.setNumber("12");
        updatedAddress.setComplement("Next to the shopping");
        updatedAddress.getCep().setValue("12345678");
        updatedAddress.getCep().setCity("New beautiful city");

        doReturn(updatedAddress.getCep())
                .when(cepSearchService)
                .findBy(updatedAddress.getCep().getValue());

        doReturn(Optional.of(buildAddress()))
                .when(addressRepository)
                .findById(id);

        Address actual = addressManagementService.updateAddress(updatedAddress, id);
        Cep actualCep = actual.getCep();
        Address expected = buildAddress();

        assertThat(actualCep.getValue(), is(equalTo(updatedAddress.getCep().getValue())));
        assertThat(actualCep.getCity(), is(equalTo(updatedAddress.getCep().getCity())));
        assertThat(actualCep.getDistrict(), is(equalTo(expected.getCep().getDistrict())));
        assertThat(actualCep.getEstate(), is(equalTo(expected.getCep().getEstate())));
        assertThat(actualCep.getStreet(), is(equalTo(expected.getCep().getStreet())));
        assertThat(actual.getComplement(), is(equalTo(updatedAddress.getComplement())));
        assertThat(actual.getNumber(), is(equalTo(updatedAddress.getNumber())));
        verify(addressRepository).save(actual);
    }

    @Test(expected = CepNotFoundException.class)
    public void shouldThrowExceptionWhenTryingToUpdateAddressWithCepNotFound() {
        Address updatedAddress = buildAddress();

        doThrow(CepNotFoundException.class)
                .when(cepSearchService)
                .findBy(updatedAddress.getCep().getValue());

        addressManagementService.updateAddress(updatedAddress, 10L);
    }

    @Test(expected = AddressNotFoundException.class)
    public void shouldThrowExceptionWhenTryingToUpdateAddressNotFound() {
        Address updatedAddress = buildAddress();

        doThrow(AddressNotFoundException.class)
                .when(addressRepository)
                .findById(15L);

        addressManagementService.updateAddress(updatedAddress, 15L);
    }

    private Address buildAddress() {
        Cep cep = buildCep();
        Address address = new Address();
        address.setCep(cep);
        address.setComplement("<<COMPLEMENT>>");
        address.setNumber("<<NUMBER>>");
        return address;
    }

    private Cep buildCep() {
        Cep cep = new Cep();
        cep.setValue("<<CEP>>");
        cep.setCity("<<CITY>>");
        cep.setDistrict("<<DISTRICT>>");
        cep.setEstate("<<ESTATE>>");
        cep.setStreet("<<STREET>>");
        return cep;
    }
}