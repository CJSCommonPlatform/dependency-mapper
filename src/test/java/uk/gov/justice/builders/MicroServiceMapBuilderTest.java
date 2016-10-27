package uk.gov.justice.builders;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class MicroServiceMapBuilderTest {

    @Test
    public void shouldReturnAMapOfMicroServices() {
        List<MicroService> microServices = Arrays.asList(
                new MicroServiceBuilder().withName("A").build(),
                new MicroServiceBuilder().withName("B").build(),
                new MicroServiceBuilder().withName("C").build(),
                new MicroServiceBuilder().withName("D").build());

        Map<MicroService, Set<MicroService>> expectedMicroServiceMap = new HashMap<>();

        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("A").build(), Collections.EMPTY_SET);
        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("B").build(), Collections.EMPTY_SET);
        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("C").build(), Collections.EMPTY_SET);
        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("D").build(), Collections.EMPTY_SET);

        Map<MicroService, Set<MicroService>> microServiceMap = new MicroServiceMapBuilder(microServices).generate();

        assertThat(microServiceMap, is(expectedMicroServiceMap));
    }

    @Test
    public void shouldReturnAMapOfMicroServicesIncludingConsumers() {
        List<MicroService> microServices = Arrays.asList(
                new MicroServiceBuilder().withName("A").withUses(Collections.singletonList(new MicroServiceBuilder().withName("C").build())).build(),
                new MicroServiceBuilder().withName("B").withUses(Collections.singletonList(new MicroServiceBuilder().withName("C").build())).build(),
                new MicroServiceBuilder().withName("C").withUses(Collections.singletonList(new MicroServiceBuilder().withName("D").build())).build(),
                new MicroServiceBuilder().withName("D").withUses(Collections.singletonList(new MicroServiceBuilder().withName("A").build())).build());

        Map<MicroService, Set<MicroService>> expectedMicroServiceMap = new HashMap<>();

        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("A").build(), Stream.of(new MicroServiceBuilder().withName("D").build()).collect(Collectors.toSet()));
        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("B").build(), Collections.EMPTY_SET);
        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("C").build(), Stream.of(new MicroServiceBuilder().withName("A").build(), new MicroServiceBuilder().withName("B").build()).collect(Collectors.toSet()));
        expectedMicroServiceMap.put(new MicroServiceBuilder().withName("D").build(), Stream.of(new MicroServiceBuilder().withName("C").build()).collect(Collectors.toSet()));

        Map<MicroService, Set<MicroService>> microServiceMap = new MicroServiceMapBuilder(microServices).generate();

        assertThat(microServiceMap, is(expectedMicroServiceMap));
    }

    @Test
    public void shouldReturnAMapOfMicroServicesIncludingConsumersAndVersion() {
        List<MicroService> microServices = Arrays.asList(
                new MicroServiceBuilder().withName("A").withVersion("5.0").withUses(Collections.singletonList(new MicroServiceBuilder().withName("C").withVersion("1.0").build())).build(),
                new MicroServiceBuilder().withName("B").withVersion("6.0").withUses(Collections.singletonList(new MicroServiceBuilder().withName("C").withVersion("2.0").build())).build(),
                new MicroServiceBuilder().withName("C").withVersion("4.0").withUses(Collections.singletonList(new MicroServiceBuilder().withName("A").withVersion("5.0").build())).build());

        Map<MicroService, Set<MicroService>> microServiceMap = new MicroServiceMapBuilder(microServices).generate();
        microServiceMap.keySet().forEach(this::assertMicroService);

        MicroService msAConsumer = microServiceMap.get(new MicroServiceBuilder().withName("A").build()).iterator().next();
        assertThat(msAConsumer.getName(), is("C"));
        assertThat(msAConsumer.getVersion(), is("5.0"));

        Set<MicroService> msBConsumers = microServiceMap.get(new MicroServiceBuilder().withName("B").build());
        assertThat(msBConsumers.isEmpty(), is(true));

        Set<MicroService> msCConsumers = microServiceMap.get(new MicroServiceBuilder().withName("C").build());
        msCConsumers.forEach(this::assertMicroServiceConsumersForMicroServiceC);
    }

    @Test
    public void shouldReturnAMapOfMicroServicesIncludingConsumersAndVersionInAnyOrder() {
        List<MicroService> microServices = Arrays.asList(
                new MicroServiceBuilder().withName("C").withVersion("4.0").withUses(Collections.singletonList(new MicroServiceBuilder().withName("A").withVersion("4.9").build())).build(),
                new MicroServiceBuilder().withName("A").withVersion("5.0").withUses(Collections.singletonList(new MicroServiceBuilder().withName("C").withVersion("1.0").build())).build(),
                new MicroServiceBuilder().withName("B").withVersion("6.0").withUses(Collections.singletonList(new MicroServiceBuilder().withName("C").withVersion("2.0").build())).build());

        Map<MicroService, Set<MicroService>> microServiceMap = new MicroServiceMapBuilder(microServices).generate();

        microServiceMap.keySet().forEach(this::assertMicroService);

        MicroService msAConsumer = microServiceMap.get(new MicroServiceBuilder().withName("A").build()).iterator().next();
        assertThat(msAConsumer.getName(), is("C"));
        assertThat(msAConsumer.getVersion(), is("4.9"));

        Set<MicroService> msBConsumers = microServiceMap.get(new MicroServiceBuilder().withName("B").build());
        assertThat(msBConsumers.isEmpty(), is(true));

        Set<MicroService> msCConsumers = microServiceMap.get(new MicroServiceBuilder().withName("C").build());
        msCConsumers.forEach(this::assertMicroServiceConsumersForMicroServiceC);
    }

    private void assertMicroService(MicroService microService) {
        if (microService.getName().equals("A")) assertThat(microService.getVersion(), is("5.0"));
        if (microService.getName().equals("B")) assertThat(microService.getVersion(), is("6.0"));
        if (microService.getName().equals("C")) assertThat(microService.getVersion(), is("4.0"));
        if (microService.getName().equals("D")) assertThat(microService.getVersion(), is("8.0"));
    }

    private void assertMicroServiceConsumersForMicroServiceC(MicroService microService) {
        if (microService.getName().equals("A")) assertThat(microService.getVersion(), is("1.0"));
        if (microService.getName().equals("B")) assertThat(microService.getVersion(), is("2.0"));
    }
}
