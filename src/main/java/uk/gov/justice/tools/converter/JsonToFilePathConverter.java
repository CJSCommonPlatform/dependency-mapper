package uk.gov.justice.tools.converter;

import uk.gov.justice.tools.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class JsonToFilePathConverter implements Converter<Path, String> {

    static final String FILENAME = "contexts.json";
    private Config config;

    public JsonToFilePathConverter(Config config) {
        this.config = config;
    }

    @Override
    public Path convert(String jsonPayload) throws IOException {
        Path path = Paths.get(config.getOutputDirectory().concat(FILENAME));
        Files.deleteIfExists(path);
        Files.createFile(path);
        return Files.write(path, jsonPayload.getBytes());
    }
}
