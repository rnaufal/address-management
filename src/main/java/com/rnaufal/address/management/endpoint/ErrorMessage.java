package com.rnaufal.address.management.endpoint;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by rnaufal on 19/11/16.
 */
public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = -5843905018731135460L;

    private final String error;

    public ErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("error", error)
                .toString();
    }
}
