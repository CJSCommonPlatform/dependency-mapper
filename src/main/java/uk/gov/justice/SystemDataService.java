package uk.gov.justice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import uk.gov.justice.builders.MicroService;
import uk.gov.justice.builders.MicroServiceMapBuilder;
import uk.gov.justice.tools.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SystemDataService {

    private final Config config;

    public SystemDataService(Config config) {
        this.config = config;
    }

    public String generate() throws Exception {
        FileFinder fileFinder = new FileFinder(config);
        List<File> pomFiles = fileFinder.findPomFiles();
        PomParser pomParser = new PomParser();
        List<MicroService> microServices = new ArrayList<>();
        for (File pomFile : pomFiles) {
            microServices.add(pomParser.parse(pomFile));
        }
        MicroServiceMapBuilder builder = new MicroServiceMapBuilder(microServices);

        SimpleModule module = new SimpleModule();
        module.addSerializer(ApplicationMap.class, new CustomSerialiser());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        ApplicationMap applicationMap = new ApplicationMap(builder.generate());

        String actualJson = mapper.writeValueAsString(applicationMap);
        return actualJson;
    }
}
