package uk.gov.justice.tools;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import uk.gov.justice.builders.MicroService;

import java.io.IOException;
import java.util.Set;

public class CustomSerialiser extends StdSerializer<ApplicationMap> {

    public CustomSerialiser() {
        this(null);
    }

    public CustomSerialiser(Class<ApplicationMap> t) {
        super(t);
    }


    @Override
    public void serialize(ApplicationMap applicationMap, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("microServices");
        jsonGenerator.writeStartArray();
        for (MicroService microService : applicationMap.getMicroServices().keySet()) {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("microService", microService.getName());
            jsonGenerator.writeStringField("version", microService.getVersion());
            jsonGenerator.writeStringField("ramlDocument", microService.getRamlDocument() == null ? "NA" : microService.getRamlDocument());
            jsonGenerator.writeStringField("servicePomVersion", microService.getServicePomVersion());

            jsonGenerator.writeFieldName("consumedBy");

            jsonGenerator.writeStartArray();
            Set<MicroService> consumedByMicroServices = applicationMap.getMicroServices().get(microService);

            for (MicroService consumedByMicroService : consumedByMicroServices) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("microService", consumedByMicroService.getName());
                jsonGenerator.writeStringField("usingVersion", consumedByMicroService.getVersion());
                jsonGenerator.writeEndObject();
            }

            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeStringField("dateCreated", applicationMap.getCreationDate());
        jsonGenerator.writeEndObject();
    }
}

