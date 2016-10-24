package uk.gov.justice.tools;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import uk.gov.justice.builder.Context;
import uk.gov.justice.builder.ContextBuilder;

public class PomParser {

    public Context parse(File pom) throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(pom));

        return new ContextBuilder()
                .withName(model.getArtifactId())
                .withVersion(model.getVersion())
                .build();
    }
}
