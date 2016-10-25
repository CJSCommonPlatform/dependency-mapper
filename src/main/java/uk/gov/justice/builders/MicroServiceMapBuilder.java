package uk.gov.justice.builders;


import sun.nio.cs.ext.MS874;

import java.util.*;

class MicroServiceMapBuilder {

    private List<MicroService> microServices;

    MicroServiceMapBuilder(List<MicroService> microServices) {
        this.microServices = microServices;
    }

    private MicroServiceMapBuilder() {
    }

    Map<MicroService, List<MicroService>> generate() {
        Map<MicroService, List<MicroService>> microServiceMap = new HashMap<>();
        microServices.forEach(microService -> process(microServiceMap, microService));
        return microServiceMap;
    }

    private void process(Map<MicroService, List<MicroService>> microServiceMap, MicroService microService) {
        if(!microServiceMap.containsKey(microService)) {
            microServiceMap.put(microService, Collections.emptyList());
        }
        microService.uses().forEach(targetMicroService -> registerUsage(microServiceMap, targetMicroService, microService));

    }

    private void registerUsage(Map<MicroService, List<MicroService>> microServiceMap, MicroService targetMicroService, MicroService usedByMicroService) {
        if(!microServiceMap.containsKey(targetMicroService)) {
            microServiceMap.put(targetMicroService, Collections.singletonList(usedByMicroService));
        } else {
            List<MicroService> consumedByMicroServices = new ArrayList<>(microServiceMap.get(targetMicroService));
            consumedByMicroServices.add(usedByMicroService);
            microServiceMap.put(targetMicroService, consumedByMicroServices);
        }
    }
}
