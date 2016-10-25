package uk.gov.justice.builder;

public class MicroServiceBuilder {

    private String name;
    private String version;

    public MicroServiceBuilder(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public MicroServiceBuilder() {
    }



    public String getName() {
        return name;
    }

    public MicroServiceBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public MicroServiceBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    public MicroService build(){
        return new MicroService(getName(), getVersion());
    }
}
