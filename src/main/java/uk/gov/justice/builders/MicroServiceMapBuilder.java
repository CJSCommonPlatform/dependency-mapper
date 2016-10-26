package uk.gov.justice.builders;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MicroServiceMapBuilder {

    private List<MicroService> microServices;

    MicroServiceMapBuilder(List<MicroService> microServices) {
        this.microServices = microServices;
    }

    private MicroServiceMapBuilder() {
    }

    Map<MicroService, List<MicroService>> generate() {
        Map<MicroService, List<MicroService>> microServiceMap = new HashMap<>();
        microServices.forEach(microService -> registerMicroService(microServiceMap, microService));
        return microServiceMap;
    }

    private void registerMicroService(Map<MicroService, List<MicroService>> microServiceMap, MicroService microService) {
        if (!microServiceMap.containsKey(microService)) {
            microServiceMap.put(microService, Collections.emptyList());
        } else {
            MicroService actualMicroService = new MicroServiceBuilder().withName(microService.getName()).withVersion(microService.getVersion()).withUses(microService.uses()).build();
            List<MicroService> alreadyRegisteredConsumers = microServiceMap.get(microService);
            microServiceMap.remove(actualMicroService);
            microServiceMap.put(actualMicroService, alreadyRegisteredConsumers);
        }
        microService.uses().forEach(targetMicroService -> registerUsage(microServiceMap, targetMicroService, microService));

    }

    private void registerUsage(Map<MicroService, List<MicroService>> microServiceMap, MicroService targetMicroService, MicroService usedByMicroService) {
        if (!microServiceMap.containsKey(targetMicroService)) {
            microServiceMap.put(new MicroServiceBuilder().withName(targetMicroService.getName()).withVersion("NA").build(),
                    Collections.singletonList(new MicroServiceBuilder().withName(usedByMicroService.getName()).withVersion(targetMicroService.getVersion()).build()));
        } else {
            List<MicroService> consumedByMicroServices = new ArrayList<>(microServiceMap.get(targetMicroService));
            consumedByMicroServices.add(new MicroServiceBuilder().withName(usedByMicroService.getName()).withVersion(targetMicroService.getVersion()).build());
            microServiceMap.put(targetMicroService, consumedByMicroServices);
        }
    }
}
