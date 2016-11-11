package uk.gov.justice.tools.service;


import uk.gov.justice.tools.Config;
import uk.gov.justice.tools.FileFinder;
import uk.gov.justice.tools.converter.Html;
import uk.gov.justice.tools.converter.RamlFileToHtml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

public class RamlReportService {
    private Config config;

    public RamlReportService(Config config) {
        this.config = config;
    }

    public void generateRamlReport() throws IOException {
        FileFinder fileFinder = new FileFinder(config.getRootDirectory());
        List<File> ramlFiles = fileFinder.findRamlFiles();

        RamlFileToHtml ramlFileToHtml = new RamlFileToHtml(config.getRamlReportDirectory());
        ramlFiles.stream().map(ramlFileToHtml::convert).forEach(writeHtmlToFile);

    }

    Consumer<Html> writeHtmlToFile = html -> {
        try {
            Path path = Paths.get(html.getFilePath());
            Files.deleteIfExists(path);
            Files.write(path, html.getHtmlAsText().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
}
