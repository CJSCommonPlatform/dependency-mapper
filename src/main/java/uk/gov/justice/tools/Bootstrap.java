package uk.gov.justice.tools;


import uk.gov.justice.tools.converter.WriteJson;
import uk.gov.justice.tools.service.DependencyMapperService;
import uk.gov.justice.tools.service.RamlReportService;

public class Bootstrap {

    private static final String ROOT_DIRECTORY =
            System.getenv("dmx.contexts.dir") != null ?
                    System.getenv("dmx.contexts.dir") : System.getProperty("dmx.contexts.dir", "/opt/contexts/");
    private static final String OUTPUT_FILE_PATH =
            System.getenv("dmx.contexts.map.file") != null ?
                    System.getenv("dmx.contexts.map.file") : System.getProperty("dmx.contexts.map.file", "/opt/contexts.json");
    private static String RAML_REPORT_DIR =
            System.getenv("dmx.raml.reports.dir") != null ?
                    System.getenv("dmx.raml.reports.dir") : System.getProperty("dmx.raml.reports.dir", "/opt/raml-reports/");


    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.setRootDirectory(ROOT_DIRECTORY);
        config.setOutputFilePath(OUTPUT_FILE_PATH);
        config.setRamlReportDirectory(RAML_REPORT_DIR);

        //prepare DependencyMapper service
        DependencyMapperService dependencyMapperService = new DependencyMapperService(config);

        WriteJson writeJson = new WriteJson(config);
        writeJson.convert(dependencyMapperService.generate());

        RamlReportService ramlReportService = new RamlReportService(config);
        ramlReportService.generateRamlReport();
    }
}