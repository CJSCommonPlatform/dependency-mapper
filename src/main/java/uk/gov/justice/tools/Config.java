package uk.gov.justice.tools;


public class Config {

    private String rootDirectory;

    private String outputFilePath;

    public String getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputDirectory) {
        this.outputFilePath = outputDirectory;
    }
}
