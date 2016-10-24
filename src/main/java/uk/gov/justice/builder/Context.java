package uk.gov.justice.builder;

/**
 * Created by arun on 24/10/2016.
 */
public class Context {

    private String name;
    private String version;

    public Context(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public Context() {
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

        Context context = (Context) o;

        if (name != null ? !name.equals(context.name) : context.name != null) return false;
        return version != null ? version.equals(context.version) : context.version == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
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
