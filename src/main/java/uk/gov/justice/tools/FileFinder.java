package uk.gov.justice.tools;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {

    private Config config;

    public FileFinder(Config config) {
        this.config = config;
    }

    public List<File> findPomFiles() {
        return new ArrayList<>();
    }

    public List<File> findRamlFiles() {
        return new ArrayList<>();
    }
}
