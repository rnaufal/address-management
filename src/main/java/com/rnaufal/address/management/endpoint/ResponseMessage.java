package com.rnaufal.address.management.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by rnaufal on 15/11/16.
 */
public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = 1451312958317130886L;

    private final String response;

    public ResponseMessage(String response) {
        this.response = response;
    }

    @JsonProperty("message")
    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("response", response)
                .toString();
    }
}
