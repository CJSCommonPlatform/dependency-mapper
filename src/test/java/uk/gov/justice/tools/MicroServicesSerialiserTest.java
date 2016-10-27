package uk.gov.justice.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Test;
import uk.gov.justice.builders.MicroService;
import uk.gov.justice.builders.MicroServiceBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.Assert.assertTrue;

public class MicroServicesSerialiserTest {

    @Test
    public void shouldCreateJsonFromMicroServices() throws IOException {

        MicroService msA = new MicroServiceBuilder().withName("A").withVersion("4.0").build();
        MicroService msB = new MicroServiceBuilder().withName("B").withVersion("5.0").build();
        MicroService msC = new MicroServiceBuilder().withName("C").withVersion("6.0").build();

        MicroService msD = new MicroServiceBuilder().withName("D").withVersion("7.0").build();
        MicroService msE = new MicroServiceBuilder().withName("E").withVersion("8.0").build();

        Map<MicroService, Set<MicroService>> msMap = new HashMap<>();

        msMap.put(msA, Stream.of(msB, msE).collect(Collectors.toSet()));
        msMap.put(msB, Stream.of(msA, msC).collect(Collectors.toSet()));
        msMap.put(msC, Stream.of(msC, msD).collect(Collectors.toSet()));
        msMap.put(msD, Stream.of(msA, msC).collect(Collectors.toSet()));
        msMap.put(msE, Stream.of(msB).collect(Collectors.toSet()));

        ApplicationMap applicationMap = new ApplicationMap(msMap);

        SimpleModule module = new SimpleModule();
        module.addSerializer(ApplicationMap.class, new CustomSerialiser());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        String actualJson = mapper.writeValueAsString(applicationMap);

        File expectedCustomizedJson = getExpectedCustomizedJson();
        String expectedJson = readFile(expectedCustomizedJson.getAbsolutePath(), Charset.defaultCharset());

        assertTrue(actualJson.contains(expectedJson));
    }

    private File getExpectedCustomizedJson() {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource("./".concat("expectedSerializedPartialData.json")).getFile());
    }
    private String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
