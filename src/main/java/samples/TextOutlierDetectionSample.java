package samples;

import rwa.odet.NeuralOutlierDetector;
import rwa.odet.OutlierDetector;
import util.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class TextOutlierDetectionSample {

    public static final String delimiter = ";";
    private static final int maxNeuronSize = 10000;
    private static char ch;

    public static void main(String[] args) throws IOException {

        StringBuilder sb = new StringBuilder();

        String sourceText = Util.readAsString("data/txtru.txt");
//                "This test will be written in network structure";

        Queue<Character> text = sourceText.chars().mapToObj(c -> (char) c).collect(Collectors.toCollection(ArrayDeque::new));

        OutlierDetector detector = new NeuralOutlierDetector(maxNeuronSize);
        detector.addAdaptiveChannel(() -> ch, Set.of('s'));

        int idx = 0;
        while (!text.isEmpty() && idx++ < 5000) {
            ch = text.poll();
            double outlier = detector.next();

            sb.append(ch).append(delimiter)
                    .append(outlier).append(delimiter)
                    .append("\n");

            System.out.println(idx);
        }

        Files.write(Path.of("src/main/resources/logs/text_outlier_output.csv"), sb.toString().getBytes());
    }

}
