package uk.gov.justice;


import uk.gov.justice.builders.MicroService;
import uk.gov.justice.tools.Config;
import uk.gov.justice.tools.FileFinder;
import uk.gov.justice.tools.converter.JsonToFilePathConverter;
import uk.gov.justice.tools.converter.MicroServiceToJsonConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bootstrap {

    private static final String ROOT_DIRECTORY =
            System.getenv("rootDirectory") != null ?
                    System.getenv("rootDirectory") : System.getProperty("rootDirectory", "/opt/");
    private static final String OUTPUT_DIRECTORY =
            System.getenv("outputDirectory") != null ?
                    System.getenv("outputDirectory") : System.getProperty("outputDirectory", "/opt/");

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.setRootDirectory(ROOT_DIRECTORY);
        config.setOutputDirectory(OUTPUT_DIRECTORY);

        //prepare DependencyMapper service
        DependencyMapperService dependencyMapperService = new DependencyMapperService(config);

        JsonToFilePathConverter jsonToFilePathConverter = new JsonToFilePathConverter(config);
        jsonToFilePathConverter.convert(dependencyMapperService.generate());
    }
}