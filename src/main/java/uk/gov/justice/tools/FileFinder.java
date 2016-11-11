package uk.gov.justice.tools;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {

    public static final String POM_XML = "pom.xml";
    public static final String RAML_EXTENSION = ".raml";
    private String rootDirectory;

    public FileFinder(String rootFolder) {
        this.rootDirectory = rootFolder;
    }

    public List<File> findPomFiles() throws IOException {
        return findFilesMatchingFilename(getRootDirectory(), POM_XML);
    }

    public List<File> findRamlFiles() throws IOException {
        return findFilesMatchingFilename(getRootDirectory(), RAML_EXTENSION);
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    private List<File> findFilesMatchingFilename(String path, String filenamePattern) throws IOException {
        List<File> files = new ArrayList<>();
        Files.find(Paths.get(path), Integer.MAX_VALUE,
                (p, bfa) -> bfa.isRegularFile() && p.normalize().toString().endsWith(filenamePattern))
                .map(p -> p.toFile())
                .forEach(files::add);
        return files;
    }
}
