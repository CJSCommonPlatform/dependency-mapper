package uk.gov.justice.tools;


import uk.gov.justice.tools.converter.WriteJson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import raml.tools.html.Raml2HtmlConverter;

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

        FileFinder fileFinder = new FileFinder(config);
        List<File> pomFiles = fileFinder.findRamlFiles();

        pomFiles.stream().map(convertRamlFileToHtml).forEach(writeHtmlToFile);

        WriteJson writeJson = new WriteJson(config);
        writeJson.convert(dependencyMapperService.generate());
    }


    private static Consumer<Html> writeHtmlToFile = html -> {
        try {
            Files.write(Paths.get(html.filePath), html.htmlAsText.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    private static Function<File, Html> convertRamlFileToHtml = file -> {
        InputStream ramlInput = null;
        try {
            ramlInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Raml2HtmlConverter raml2HtmlConverter = new Raml2HtmlConverter();
        raml2HtmlConverter.withRamlBasePath(file.getParent());
        return new Html(raml2HtmlConverter.convert(ramlInput, new ByteArrayOutputStream()).toString(),
                "c:/temp/ramltohtml/" + file.getName().replace("raml", "html"));
    };

    private static class Html {
        private String htmlAsText;
        private String filePath;

        public Html(String htmlAsText, String filePath) {
            this.htmlAsText = htmlAsText;
            this.filePath = filePath;
        }
    }
}