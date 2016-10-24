package uk.gov.justice.tools;


import java.io.File;

public class PomParserException extends Exception{

    public PomParserException(String message) {
        super(message);
    }

    public PomParserException(File pomFile, String message, Throwable cause) {
        super(pomFile != null && pomFile.getAbsolutePath() != null ?
                        "Error encountered when processing file ' "
                                .concat(pomFile.getAbsolutePath())
                                .concat(" ' --> ")
                                .concat(message) : message, cause);
    }

    public PomParserException(File pomFile, String message) {
        super(pomFile != null && pomFile.getAbsolutePath() != null ?
                "Error encountered when processing file ' "
                        .concat(pomFile.getAbsolutePath())
                        .concat(" ' --> ")
                        .concat(message) : message);
    }
}
