package uk.gov.justice.builders;

import java.util.List;

public class MicroService {

    private String name;
    private String version;
    private List<MicroService> uses;
    private String ramlDocument;
    private String servicePomVersion;

    public MicroService(String name, String version, List<MicroService> uses, String ramlDocument, String servicePomVersion) {
        this.name = name;
        this.version = version;
        this.uses = uses;
        this.ramlDocument = ramlDocument;
        this.servicePomVersion = servicePomVersion;
    }

    public MicroService() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServicePomVersion() {
        return servicePomVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MicroService microService = (MicroService) o;

        if (!name.equals(microService.name)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MicroService{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    public List<MicroService> uses() {
        return uses;
    }

    public String getRamlDocument() {
        return ramlDocument;
    }

    public void setRamlDocument(String ramlDocument) {
        this.ramlDocument = ramlDocument;
    }
}
