package util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public class Util {
    private Util() {
    }

    public static String readResourceFile(String fileName) {
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

}
