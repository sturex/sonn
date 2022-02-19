package playground;

import neural.Network;
import neural.NetworkEventsListener;
import util.patgen.Pattern;
import util.patgen.PatternGenerator;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class AssociationPlayground {

    private static final Random random = new Random();
    public static final int bound = 2;

    public static final int patternSize = 200;
    public static final int patternLength = 10;
    public static final int maxNeuronSize = patternLength * 3;
    public static int[] inputArray;
    private static int idx;

    public static void main(String[] args) throws InterruptedException {

        Pattern pattern1 = Pattern.randomized("p1", patternSize, patternLength, bound);
        Pattern pattern2 = Pattern.randomized("p2", patternSize, patternLength, bound);
        Pattern pattern3 = Pattern.randomized("p3", patternSize, patternLength, bound);

        PatternGenerator patternGenerator = PatternGenerator.newBuilder(patternSize)
                .addPattern(pattern1, 1, 0.0)
                .addPattern(pattern2, 1, 0.0)
                .addPattern(pattern3, 1, 0.0)
//                .addPattern(Pattern.randomized(patternSize, patternLength, 2), 1, 0.0)
                .withProbability(1)
                .build();

        List<NetworkEventsListener> listeners = List.of(new LayoutAdapter(new GraphStreamStaticLayout()));
//        List<NetworkEventsListener> listeners = List.of(new LayoutAdapter(new GraphStreamStaticLayout()),
//                new LayoutAdapter(new GraphStreamDynamicLayout()));

        Network network = new Network(listeners, maxNeuronSize);

        for (int idx = 0; idx < patternSize; idx++) {
            int channelIdx = idx;
//            network.addStrictDictReceptor(() -> inputArray[channelIdx], Set.of(0));
            network.addReceptor(() -> inputArray[channelIdx] <= 0);
        }

        IntStream.range(0, patternLength * 3).forEach(rdx -> network.addReflex(() -> idx == rdx && idx != 0, () -> System.out.print(" "+ rdx + "->" + idx + " ")));
//        network.addPainReceptor(() -> idx < patternLength * 3);

        train(network, pattern1);
        train(network, pattern2);
        train(network, pattern3);

        for (int[] ints : patternGenerator) {
            inputArray = ints;
            System.out.print(idx++ + ": ");
            network.tick();
            System.out.println();
            Thread.sleep(200);
        }


    }

    private static void train(Network network, Pattern pattern) throws InterruptedException {
        String patName = pattern.getPatName();
//        int idx = 0;
        for (int[] pat : pattern) {
            inputArray = pat;
            int sdx = idx;
//            network.tick();
            network.tick();
            idx++;
            System.out.println();
            Thread.sleep(200);
        }
    }
}
