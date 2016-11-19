package com.rnaufal.address.management.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by rnaufal on 15/11/16.
 */
public class ErrorMessages implements Serializable {

    private static final long serialVersionUID = 1451312958317130886L;

    private final List<ErrorMessage> errors;

    public ErrorMessages(List<ErrorMessage> errors) {
        this.errors = Collections.unmodifiableList(errors);
    }

    public ErrorMessages(ErrorMessage error) {
        this.errors = Collections.singletonList(error);
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("errors", errors)
                .toString();
    }
}
