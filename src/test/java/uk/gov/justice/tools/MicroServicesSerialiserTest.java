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

import static junit.framework.Assert.assertTrue;

public class MicroServicesSerialiserTest {

    private String expectedCustomizedJson = "{\"microServices\":[{\"microService\":\"A\",\"version\":\"4.0\",\"consumedBy\":[{\"microService\":\"B\",\"usingVersion\":\"5.0\"},{\"microService\":\"E\",\"usingVersion\":\"8.0\"}]},{\"microService\":\"B\",\"version\":\"5.0\",\"consumedBy\":[{\"microService\":\"A\",\"usingVersion\":\"4.0\"},{\"microService\":\"C\",\"usingVersion\":\"6.0\"}]},{\"microService\":\"C\",\"version\":\"6.0\",\"consumedBy\":[{\"microService\":\"C\",\"usingVersion\":\"6.0\"},{\"microService\":\"D\",\"usingVersion\":\"7.0\"}]},{\"microService\":\"D\",\"version\":\"7.0\",\"consumedBy\":[{\"microService\":\"A\",\"usingVersion\":\"4.0\"},{\"microService\":\"C\",\"usingVersion\":\"6.0\"}]},{\"microService\":\"E\",\"version\":\"8.0\",\"consumedBy\":[{\"microService\":\"B\",\"usingVersion\":\"5.0\"}]}]}";

    @Test
    public void shouldCreateJsonFromMicroServices() throws IOException {

        MicroService msA = new MicroServiceBuilder().withName("A").withVersion("4.0").build();
        MicroService msB = new MicroServiceBuilder().withName("B").withVersion("5.0").build();
        MicroService msC = new MicroServiceBuilder().withName("C").withVersion("6.0").build();

        MicroService msD = new MicroServiceBuilder().withName("D").withVersion("7.0").build();
        MicroService msE = new MicroServiceBuilder().withName("E").withVersion("8.0").build();

        Map<MicroService, List<MicroService>> msMap = new HashMap<>();

        msMap.put(msA, Arrays.asList(msB, msE));
        msMap.put(msB, Arrays.asList(msA, msC));
        msMap.put(msC, Arrays.asList(msC, msD));
        msMap.put(msD, Arrays.asList(msA, msC));
        msMap.put(msE, Collections.singletonList(msB));

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
