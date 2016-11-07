package uk.gov.justice.tools.converter;

import uk.gov.justice.tools.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class WriteJson implements Converter<Path, String> {

    private Config config;

    public WriteJson(Config config) {
        this.config = config;
    }

    @Override
    public Path convert(String jsonPayload) {
        Path path = Paths.get(config.getOutputFilePath());
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
            return Files.write(path, jsonPayload.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
