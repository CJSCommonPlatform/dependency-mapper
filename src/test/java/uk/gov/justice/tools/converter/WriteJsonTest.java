package uk.gov.justice.tools.converter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Test;

import uk.gov.justice.tools.Config;

public class WriteJsonTest {

	@Test
	public void convert() throws Exception {

		Path tempDir = Files.createTempDirectory("dmx");

		Config config = new Config();
		config.setOutputFilePath(tempDir.toString() + "/contexts.json");

		WriteJson testObj = new WriteJson(config);

		Path path = testObj.convert("{\"name\":\"abc\",\"version\":\"1.1\"}");

		assertThat(new String(Files.readAllBytes(path)), is("{\"name\":\"abc\",\"version\":\"1.1\"}"));

		recursiveDeleteOnExit(tempDir);

	}

	public static void recursiveDeleteOnExit(Path rootPath) throws IOException {
		Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, @SuppressWarnings("unused") BasicFileAttributes attrs) {
				file.toFile().delete();
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, @SuppressWarnings("unused") BasicFileAttributes attrs) {
				dir.toFile().delete();
				return FileVisitResult.CONTINUE;
			}
		});
		Files.delete(rootPath);
	}

}