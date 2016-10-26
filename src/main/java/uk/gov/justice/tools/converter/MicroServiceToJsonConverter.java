package uk.gov.justice.tools.converter;


import uk.gov.justice.builders.MicroService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class MicroServiceToJsonConverter implements Converter<String, MicroService> {

    ObjectMapper objectMapper = new ObjectMapper();

    public MicroServiceToJsonConverter() {
        objectMapper.registerModule(new Jdk8Module());
    }

    public String convert(MicroService from) throws JsonProcessingException {
        return objectMapper.writeValueAsString(from);
    }
}
