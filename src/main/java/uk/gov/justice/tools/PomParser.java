package uk.gov.justice.tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import uk.gov.justice.builders.MicroService;
import uk.gov.justice.builders.MicroServiceBuilder;

public class PomParser {

    private static final String SERVICE_PARENT_POM = "service-parent-pom";
    private Logger logger = Logger.getLogger(PomParser.class.getName());
    private static String VERSION_NOT_AVAILABLE = "NA";
    private static String PROJECT_VERSION = "project.version";


    public MicroService parse(File pom) throws Exception {
        Model model = extractModel(pom);

        //name
        String artifactId = model.getArtifactId();
        if (StringUtils.isBlank(artifactId))
            throw new PomParserException(pom, "ArtifactId is missing form pom file [" + pom.getAbsolutePath() + "]");

        //version
        String version = model.getVersion();
        version = StringUtils.isNotBlank(version) ? version : model.getParent().getVersion();

        //uses
        List<MicroService> usesMicroServices = new ArrayList<MicroService>();
        Build build = model.getBuild();
        if (build != null) {
            build.getPlugins().forEach(plugin -> plugin.getDependencies()
                    .forEach(dependency -> {
                        try {
                            addDependency(usesMicroServices, dependency, model);
                        } catch (Exception e) {
                            logger.log(Level.WARNING, e.getLocalizedMessage());
                        }
                    }));
        }

        //framework Version
        String servicePomVersion = getServicePomVersion(model);

        return new MicroServiceBuilder()
                .withName(artifactId)
                .withVersion(version)
                .withServicePomVersion(servicePomVersion)
                .withUses(usesMicroServices)
                .build();
    }

    private Model extractModel(File pom) throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(pom));
        model.setPomFile(pom);
        return model;
    }

    private void addDependency(List<MicroService> usesMicroServices, Dependency dependency, Model model) throws Exception {
        if (dependency != null && dependency.getClassifier() != null && dependency.getClassifier().equals("raml")) {
            usesMicroServices.add(new MicroServiceBuilder().withName(dependency.getArtifactId()).withVersion(fetchDependencyVersion(dependency.getVersion(), model)).build());
        }
    }

    private String getServicePomVersion(Model model) throws XmlPullParserException {
        if (model.getParent() == null) {
            return VERSION_NOT_AVAILABLE;
        }

        if (StringUtils.equals(model.getParent().getArtifactId(), SERVICE_PARENT_POM)) {
            return model.getParent().getVersion();
        } else {
            File parentPom = new File(model.getProjectDirectory().getPath() + '/' + model.getParent().getRelativePath());
            Model parentModel = null;
            try {
                parentModel = extractModel(parentPom);
            } catch (IOException e) {
                //Parent POM is not found
                return VERSION_NOT_AVAILABLE;
            }
            return getServicePomVersion(parentModel);
        }
    }

    private String fetchDependencyVersion(String dependencyVersion, Model model) throws Exception {

        MavenProject mavenProject = new MavenProject(model);

        // Check if no version specified
        if (dependencyVersion == null) {
            return VERSION_NOT_AVAILABLE;
        } else if (isVariable(dependencyVersion)) {
            String versionFromPom = parseVariableName(dependencyVersion);
            if (versionFromPom.equals(PROJECT_VERSION)) {
                return mavenProject.getVersion();
            } else {
                String propertyVersionValue = mavenProject.getProperties().getProperty(versionFromPom);
                //deal with variable version from parents
                propertyVersionValue = propertyVersionValue == null ?
                        fetchVersionFromParents(model.getPomFile(), versionFromPom) : propertyVersionValue;
                return propertyVersionValue != null ? propertyVersionValue.toString() : dependencyVersion;
            }
        } else {
            return dependencyVersion;
        }
    }

    private boolean isVariable(String variableName) {
        Pattern anyVariablePattern = Pattern.compile("([${]+.)+\\w+");
        Matcher anyVariableMatcher = anyVariablePattern.matcher(variableName);
        return anyVariableMatcher.find();
    }

    private String parseVariableName(String variableName) {
        Pattern variableNamePattern = Pattern.compile("\\$\\{((?:[a-z]+\\.?)+)\\}");
        Matcher variableNameMatcher = variableNamePattern.matcher(variableName);
        if (variableNameMatcher.find()) {
            return variableNameMatcher.group(1);
        } else {
            return VERSION_NOT_AVAILABLE;
        }
    }

    public Properties fetchParentPomProperties(File somePom) throws Exception {
        Properties props = new Properties();
        if (somePom != null && somePom.isFile()) {
            File parent = somePom.getParentFile().getParentFile();
            if (parent.isDirectory()) {
                File parentPom = new File(parent.getAbsolutePath().concat(File.separator).concat("pom.xml"));
                if (parentPom.isFile() && parentPom.exists()) {
                    Model model = extractModel(parentPom);
                    MavenProject project = new MavenProject(model);
                    return project.getProperties();
                }
            }
        }
        return props;
    }

    /**
     * This function will work out version value from either Parent or Grand parent level POM properties if version
     * variable is found
     *
     * @param somePom
     * @param versionKey
     * @return
     * @throws Exception
     */
    public String fetchVersionFromParents(File somePom, String versionKey) throws Exception {
        Properties parentPomProperties = fetchParentPomProperties(somePom);
        if (parentPomProperties.containsKey(versionKey))
            return parentPomProperties.getProperty(versionKey);
        else {
            File parent = somePom.getParentFile().getParentFile();
            Properties grandParentProps = fetchParentPomProperties(new File(parent.getAbsolutePath().concat(File.separator).concat("pom.xml")));
            if (grandParentProps.containsKey(versionKey)) {
                return grandParentProps.getProperty(versionKey);
            }
        }
        return versionKey;
    }
}
