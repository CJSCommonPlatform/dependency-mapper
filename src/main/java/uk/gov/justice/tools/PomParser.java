package uk.gov.justice.tools;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.File;
import java.io.FileReader;

import uk.gov.justice.builder.Context;
import uk.gov.justice.builder.ContextBuilder;

public class PomParser {

    public Context parse(File pom) throws Exception {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(pom));

        //name
        String artifactId = model.getArtifactId();
        if(StringUtils.isBlank(artifactId))
            throw new PomParserException(pom, "ArtifactId is missing");

        //version
        String version = model.getVersion();
        version = StringUtils.isNotBlank(version) ? version : model.getParent().getVersion();

        return new ContextBuilder()
                .withName(artifactId)
                .withVersion(version)
                .build();
    }
}
