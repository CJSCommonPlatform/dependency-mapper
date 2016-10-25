package uk.gov.justice.builders;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MicroServiceMapBuilderTest {

    @Test
    public void shouldReturnAMapOfMicroServices() {
        List<MicroService> microServices = Arrays.asList(
                new MicroServiceBuilder().withName("A").build(),
                new MicroServiceBuilder().withName("B").build(),
                new MicroServiceBuilder().withName("C").build(),
                new MicroServiceBuilder().withName("D").build());

        Map<MicroService, List<MicroService>> expectedMicroServiceMap = new HashMap<>();

        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("A").build(), Collections.emptyList());
        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("B").build(), Collections.emptyList());
        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("C").build(), Collections.emptyList());
        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("D").build(), Collections.emptyList());

        Map<MicroService, List<MicroService>> microServiceMap = new MicroServiceMapBuilder(microServices).generate();

        assertThat(microServiceMap, is(expectedMicroServiceMap));
    }
}
