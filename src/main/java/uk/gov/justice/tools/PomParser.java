package uk.gov.justice.tools;

import org.apache.maven.project.MavenProject;
import uk.gov.justice.builders.MicroService;
import uk.gov.justice.builders.MicroServiceBuilder;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

public class PomParser {

    public static final String PEOPLE_CONTEXT = "people";
    public static final String MATERIAL_CONTEXT = "material";
    public static final String STRUCTURE_CONTEXT = "structure";
    public static final String CHARGING_CONTEXT = "charging";
    public static final String ASSIGNMENT_CONTEXT = "assignment";
    public static final String SCHEDULING_CONTEXT = "scheduling";
    public static final String PROGRESSION_CONTEXT = "progression";

    public MicroService parse(File pom) throws Exception {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(pom));
        model.setPomFile(pom);

        //name
        String artifactId = model.getArtifactId();
        if (StringUtils.isBlank(artifactId))
            throw new PomParserException(pom, "ArtifactId is missing");

        //version
        String version = model.getVersion();
        version = StringUtils.isNotBlank(version) ? version : model.getParent().getVersion();

        MavenProject project = new MavenProject(model);
        Properties properties = project.getProperties();

        //uses
        List<MicroService> usesMicroServices = new ArrayList<MicroService>();
        Build build = model.getBuild();
        if (build != null) {
            build.getPlugins().forEach(plugin -> plugin.getDependencies()
                    .forEach(dependency -> addDependency(usesMicroServices, dependency, properties)));
        }

        return new MicroServiceBuilder()
                .withName(artifactId)
                .withVersion(version)
                .withUses(usesMicroServices)
                .build();
    }

    private void addDependency(List<MicroService> usesMicroServices, Dependency dependency, Properties properties) {
        if (dependency != null && dependency.getClassifier() != null && dependency.getClassifier().equals("raml")) {
            usesMicroServices.add(new MicroServiceBuilder().withName(dependency.getArtifactId()).withVersion(fetchDependencyVersion(dependency, properties)).build());
        }
    }

    private String fetchDependencyVersion(Dependency dependency, Properties properties) {
        if (!dependency.getVersion().isEmpty() &&
                dependency.getVersion().startsWith("$")){
            for(String key : properties.stringPropertyNames()){
                if(key.startsWith(PEOPLE_CONTEXT) && dependency.getArtifactId().startsWith(PEOPLE_CONTEXT))
                    return properties.getProperty(key);
                if(key.startsWith(MATERIAL_CONTEXT) && dependency.getArtifactId().startsWith(MATERIAL_CONTEXT))
                    return properties.getProperty(key);
                if(key.startsWith(STRUCTURE_CONTEXT) && dependency.getArtifactId().startsWith(STRUCTURE_CONTEXT))
                    return properties.getProperty(key);
                if(key.startsWith(CHARGING_CONTEXT) && dependency.getArtifactId().startsWith(CHARGING_CONTEXT))
                    return properties.getProperty(key);
                if(key.startsWith(ASSIGNMENT_CONTEXT) && dependency.getArtifactId().startsWith(ASSIGNMENT_CONTEXT))
                    return properties.getProperty(key);
                if(key.startsWith(SCHEDULING_CONTEXT) && dependency.getArtifactId().startsWith(SCHEDULING_CONTEXT))
                    return properties.getProperty(key);
                if(key.startsWith(PROGRESSION_CONTEXT) && dependency.getArtifactId().startsWith(PROGRESSION_CONTEXT))
                    return properties.getProperty(key);
            }
        }
        return "NA";
    }
}
