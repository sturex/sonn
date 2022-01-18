package util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Util {
    private Util() {
    }

    public static String readAsString(String fileName) {
        URL resource = Util.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File not found! " + fileName);
        } else {
            try {
                return Files.readString(new File(resource.toURI()).toPath());
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException("Couldn't read resource file");
            }
        }
    }

    public static double[][] readAsDoubleArray(Path path, String delimiter) {
        try {
            return Files.lines(path)
                    .map(line -> line.split(delimiter))
                    .map(vec -> Stream.of(vec).mapToDouble(Double::parseDouble).toArray())
                    .toArray(double[][]::new);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read file: " + path);
        }
    }

}
