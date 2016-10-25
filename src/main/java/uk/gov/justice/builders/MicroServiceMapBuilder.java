package uk.gov.justice.builders;


import java.util.*;

public class MicroServiceMapBuilder {

    private List<MicroService> microServices;

    public MicroServiceMapBuilder(List<MicroService> microServices) {
        this.microServices = microServices;
    }

    private MicroServiceMapBuilder() {
    }

    public Map<MicroService, List<MicroService>> generate() {
        Map<MicroService, List<MicroService>> map = new HashMap<>();
        microServices.forEach(microService -> map.put(microService, microService.uses() == null ? Collections.emptyList() : microService.uses()));
        return map;
    }
}
