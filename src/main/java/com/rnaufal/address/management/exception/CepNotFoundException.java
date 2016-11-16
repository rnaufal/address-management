package com.rnaufal.address.management.exception;

/**
 * Created by rnaufal on 15/11/16.
 */
public class CepNotFoundException extends AddressException {

    private static final long serialVersionUID = -7552522406770398025L;

    public CepNotFoundException(String cep) {
        super(String.format("Cep=[%s] nao encontrado", cep));
    }
}
