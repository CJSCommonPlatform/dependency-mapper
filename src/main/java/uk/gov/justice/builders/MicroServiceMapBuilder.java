package uk.gov.justice.builders;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MicroServiceMapBuilder {

    private List<MicroService> microServices;

    public MicroServiceMapBuilder(List<MicroService> microServices) {
        this.microServices = microServices;
    }

    private MicroServiceMapBuilder() {
    }

    public Map<MicroService, Set<MicroService>> generate() {
        Map<MicroService, Set<MicroService>> microServiceMap = new HashMap<>();
        microServices.forEach(microService -> registerMicroService(microServiceMap, microService));
        return microServiceMap;
    }

    private void registerMicroService(Map<MicroService, Set<MicroService>> microServiceMap, MicroService microService) {
        if (!microServiceMap.containsKey(microService)) {
            microServiceMap.put(microService, Collections.EMPTY_SET);
        } else {
            MicroService actualMicroService = new MicroServiceBuilder()
                    .withName(microService.getName())
                    .withVersion(microService.getVersion())
                    .withUses(microService.uses())
                    .withServicePomVersion(microService.getServicePomVersion())
                    .withRamlDocument(microService.getRamlDocument())
                    .build();

            Set<MicroService> alreadyRegisteredConsumers = microServiceMap.get(microService);
            microServiceMap.remove(actualMicroService);
            microServiceMap.put(actualMicroService, alreadyRegisteredConsumers);
        }
        microService.uses().forEach(targetMicroService -> registerUsage(microServiceMap, targetMicroService, microService));

    }

    private void registerUsage(Map<MicroService, Set<MicroService>> microServiceMap, MicroService targetMicroService, MicroService usedByMicroService) {
        if (!microServiceMap.containsKey(targetMicroService)) {
            microServiceMap.put(new MicroServiceBuilder().withName(targetMicroService.getName()).withVersion("NA").build(),
                    Stream.of(new MicroServiceBuilder().withName(usedByMicroService.getName()).withVersion(targetMicroService.getVersion()).build()).collect(Collectors.toSet()));
        } else {
            Set<MicroService> consumedByMicroServices = new HashSet<>(microServiceMap.get(targetMicroService));
            consumedByMicroServices.add(new MicroServiceBuilder().withName(usedByMicroService.getName()).withVersion(targetMicroService.getVersion()).build());
            microServiceMap.put(targetMicroService, consumedByMicroServices);
        }
    }
}
