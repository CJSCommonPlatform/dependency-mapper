package uk.gov.justice.builder;

public class MicroService {

    private String name;
    private String version;

    public MicroService(String name, String version) {
        this.name = name;
        this.version = version;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MicroService microService = (MicroService) o;

        if (!name.equals(microService.name)) return false;
        return version != null ? version.equals(microService.version) : microService.version == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Context{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
