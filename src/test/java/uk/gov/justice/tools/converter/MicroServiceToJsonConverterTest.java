package uk.gov.justice.tools.converter;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.builder.MicroService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

public class MicroServiceToJsonConverterTest {

    @Test
    public void convert() throws JsonProcessingException {

        MicroService microService = new MicroService();
        microService.setName("abc");
        microService.setVersion("1.1");

        MicroServiceToJsonConverter testObj = new MicroServiceToJsonConverter();

        String result = testObj.convert(microService);

        assertThat(result, is("{\"name\":\"abc\",\"version\":\"1.1\"}"));
    }
}
