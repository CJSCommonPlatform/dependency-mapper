package uk.gov.justice.tools;


import java.io.File;

class PomParserException extends Exception {
    static final String DEFAULT_MESSAGE = "An error occurred while processing a POM file, however neither file nor error was captured";
    private static final String ERROR_MESSAGE = "Error encountered when processing file ' %s ' --> %s";

    PomParserException(File pomFile, String message) {
        super(pomFile != null ? String.format(ERROR_MESSAGE,
                pomFile.getAbsolutePath(), message != null ? message : DEFAULT_MESSAGE) : message != null ? message : DEFAULT_MESSAGE);
    }
}
