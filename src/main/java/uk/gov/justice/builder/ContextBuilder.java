package uk.gov.justice.builder;

/**
 * Created by arun on 24/10/2016.
 */
public class ContextBuilder {

    private String name;
    private String version;

    public ContextBuilder(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public ContextBuilder() {
    }



    public String getName() {
        return name;
    }

    public ContextBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ContextBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    public Context build(){
        return new Context(getName(), getVersion());
    }
}
