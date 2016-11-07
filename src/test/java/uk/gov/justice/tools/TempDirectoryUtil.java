package uk.gov.justice.tools;


import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class TempDirectoryUtil {

    public static Path createTempDirectory(String directoryName) throws IOException {
        return Files.createTempDirectory(directoryName);
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
