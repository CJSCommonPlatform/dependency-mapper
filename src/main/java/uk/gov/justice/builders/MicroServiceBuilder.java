package uk.gov.justice.builders;

import java.util.Collections;
import java.util.List;

public class MicroServiceBuilder {

    private String name;
    private String version;
    private List<MicroService> uses = Collections.emptyList();

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

    public List<MicroService> getUses() {
        return uses;
    }

    public MicroServiceBuilder withUses(List<MicroService> uses) {
        this.uses = uses;
        return this;
    }

    public MicroService build() {
        return new MicroService(getName(), getVersion(), getUses());
    }
}
