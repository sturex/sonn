package samples;

import rwa.patrec.NeuralPatternDetector;
import rwa.patrec.PatternDetector;
import util.patgen.Pattern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PatternRecognitionSample {

    public static final String delimiter = ";";
    public static final int maxNeuronSize = 100;
    public static final int patternSize = 10;
    public static final int patternLength = 100;
    public static int[] inputArray;
    public static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws InterruptedException, IOException {

        Pattern knownPattern = Pattern.randomized(patternSize, patternLength, 5);
        Pattern unknownPattern = Pattern.randomized(patternSize, patternLength, 5);

        PatternDetector detector = new NeuralPatternDetector(maxNeuronSize);


        for (int idx = 0; idx < patternSize; idx++) {
            int channelIdx = idx;
            detector.addAdaptiveDictChannel(() -> inputArray[channelIdx], Set.of(0));
        }

        for (int[] ints : knownPattern) {
            inputArray = ints;
            int[] recognizedPatternIds = detector.next();
            sbLog(inputArray, recognizedPatternIds);
        }
        detector.reset();

        for (int[] ints : unknownPattern) {
            inputArray = ints;
            int[] recognizedPatternIds = detector.next();
            sbLog(inputArray, recognizedPatternIds);
        }
        detector.reset();

        for (int[] ints : knownPattern) {
            inputArray = ints;
            int[] recognizedPatternIds = detector.next();
            sbLog(inputArray, recognizedPatternIds);
        }

        Files.write(Path.of("src/main/resources/logs/patrec_output.csv"), sb.toString().getBytes());
    }

    private static void sbLog(int[] inputArray, int[] recognizedPatternIds) {
        sb.append(Arrays.stream(inputArray).mapToObj(String::valueOf).collect(Collectors.joining(delimiter))).append(delimiter)
                .append(Arrays.stream(recognizedPatternIds).mapToObj(String::valueOf).collect(Collectors.joining(delimiter))).append(delimiter)
                .append("\n");
    }


}
