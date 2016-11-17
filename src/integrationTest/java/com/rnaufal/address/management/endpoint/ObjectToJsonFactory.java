package com.rnaufal.address.management.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by rnaufal on 16/11/16.
 */
public class ObjectToJsonFactory {

    private ObjectToJsonFactory() {

    }

    public static <T> String toJson(T object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(object);
    }
}
