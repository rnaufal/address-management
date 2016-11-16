package com.rnaufal.address.management.exception;

/**
 * Created by rnaufal on 15/11/16.
 */
public class AddressNotFoundException extends AddressException {

    private static final long serialVersionUID = -5296844537809483991L;

    public AddressNotFoundException(Long id) {
        super(String.format("Endereco com id=[%s] nao encontrado", id));
    }
}
