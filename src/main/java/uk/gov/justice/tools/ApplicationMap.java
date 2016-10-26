package uk.gov.justice.tools;

import uk.gov.justice.builders.MicroService;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class ApplicationMap {

    private final Map<MicroService, List<MicroService>> msMap;

    public ApplicationMap(Map<MicroService, List<MicroService>> msMap) {
       this.msMap = msMap;
    }

    public Map<MicroService, List<MicroService>> getMicroServices() {
        return msMap;
    }

    public String getCreationDate() {
        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("Europe/London");
        return ZonedDateTime.ofInstant(now, zoneId).toLocalDateTime().toString();
    }

    public String getName() {
        return "CPP";
    }
}
