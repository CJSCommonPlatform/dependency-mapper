package uk.gov.justice.tools.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import uk.gov.justice.builders.MicroService;
import uk.gov.justice.builders.MicroServiceMapBuilder;
import uk.gov.justice.tools.*;
import uk.gov.justice.tools.converter.WriteJson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DependencyMapperService {

    private final Config config;

    public DependencyMapperService(Config config) {
        this.config = config;
    }

    public String generate() throws Exception {
        FileFinder fileFinder = new FileFinder(config.getRootDirectory());
        List<File> pomFiles = fileFinder.findPomFiles();

        if (pomFiles.isEmpty())
            return "";

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

        String jsonPayload = mapper.writeValueAsString(applicationMap);
        return jsonPayload;
    }

    public void generateDependencyMapData() throws Exception {
        //write the map
        WriteJson writeJson = new WriteJson(config);
        writeJson.convert(generate());
    }
}
