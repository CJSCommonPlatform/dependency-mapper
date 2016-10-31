package uk.gov.justice.tools;


import uk.gov.justice.tools.converter.JsonToFilePathConverter;

public class Bootstrap {

    private static final String ROOT_DIRECTORY =
            System.getenv("rootDirectory") != null ?
                    System.getenv("rootDirectory") : System.getProperty("rootDirectory", "/opt/");
    private static final String OUTPUT_FILE_PATH =
            System.getenv("outputFilePath") != null ?
                    System.getenv("outputFilePath") : System.getProperty("outputFilePath", "/opt/contexts.json");

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.setRootDirectory(ROOT_DIRECTORY);
        config.setOutputFilePath(OUTPUT_FILE_PATH);

        //prepare DependencyMapper service
        DependencyMapperService dependencyMapperService = new DependencyMapperService(config);

        JsonToFilePathConverter jsonToFilePathConverter = new JsonToFilePathConverter(config);
        jsonToFilePathConverter.convert(dependencyMapperService.generate());
    }
}