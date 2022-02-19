package samples;

import neural.Bounds;
import rwa.odet.NeuralOutlierDetector;
import rwa.odet.OutlierDetector;
import util.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class OutlierDetectionSample {

    public static final String delimiter = ";";
    private static int idx = 0;
    private static final int bucketCount = 200;
    private static final Bounds bounds = new Bounds(-4, 4);
    private static final int maxNeuronSize = 1000;

    public static void main(String[] args) throws IOException {

        StringBuilder sb = new StringBuilder();

        //all columns have an anomaly starting at point 4000 with 200-points length
        double[][] data = Util.readAsDoubleArray(Path.of("src/main/resources/data/ecg.csv"), delimiter);

        OutlierDetector detector = new NeuralOutlierDetector(maxNeuronSize);
        detector.addChannel(() -> data[idx][0], bounds, bucketCount);
        detector.addChannel(() -> data[idx][1], bounds, bucketCount);
//        detector.addChannel(() -> data[idx][2], bounds, bucketCount);

        OutlierDetector detector1 = new NeuralOutlierDetector(maxNeuronSize);
        detector1.addChannel(() -> data[idx][0], bounds, bucketCount);

        OutlierDetector detector2 = new NeuralOutlierDetector(maxNeuronSize);
        detector2.addChannel(() -> data[idx][1], bounds, bucketCount);

//        OutlierDetector detector3 = new NeuralOutlierDetector(maxNeuronSize);
//        detector3.addChannel(() -> data[idx][2], bounds, bucketCount);

        int total = data.length - 1;
        for (; idx < total; idx++) {
            System.out.println(idx + " of " + total);

            double outlier = detector.next();
            double outlier1 = detector1.next();
            double outlier2 = detector2.next();
//            double outlier3 = detector3.next();

            sb.append(idx).append(delimiter)
                    .append(Arrays.stream(data[idx]).mapToObj(String::valueOf).collect(Collectors.joining(delimiter))).append(delimiter)
                    .append(outlier).append(delimiter)
                    .append(outlier1).append(delimiter)
                    .append(outlier2).append(delimiter)
//                    .append(outlier3).append(delimiter)
                    .append("\n");
        }

        Files.write(Path.of("src/main/resources/logs/ecg_o.csv"), sb.toString().getBytes());
    }

}
