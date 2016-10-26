package uk.gov.justice.tools.converter;

import uk.gov.justice.tools.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class JsonToFilePathConverter implements Converter<Path, String> {

    private Config config;
    static final String FILENAME = "contexts.json";

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
