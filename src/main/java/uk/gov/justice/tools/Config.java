package uk.gov.justice.tools;


public class Config {

    private String rootDirectory;

    private String outputFilePath;
    private String ramlReportDirectory;

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

    public void setRamlReportDirectory(String ramlReportDirectory) {
        this.ramlReportDirectory = ramlReportDirectory;
    }

    public String getRamlReportDirectory() {
        return ramlReportDirectory;
    }
}
