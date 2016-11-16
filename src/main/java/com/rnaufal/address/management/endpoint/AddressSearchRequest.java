package com.rnaufal.address.management.endpoint;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Pattern;

/**
 * Created by rnaufal on 14/11/16.
 */
public class AddressSearchRequest {

    private String cep;

    @Pattern(regexp = "\\d{8}", message = "{error.address.cep.invalid}")
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("cep", cep)
                .toString();
    }
}
