package playground;

import neural.Network;
import util.patgen.Pattern;
import util.patgen.PatternGenerator;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.Arrays;
import java.util.Set;

public class PatternRecognitionPlayground {

    public static final int maxNeuronSize = 200;
    public static final int patternSize = 50;
    public static final int patternLength = 100;
    public static int[] inputArray;

    public static void main(String[] args) throws InterruptedException {

        PatternGenerator patternGenerator = PatternGenerator.newBuilder(patternSize)
                .addPattern(Pattern.randomized(patternSize, patternLength, 2), 1, 0)
                .withLimit(1000)
                .withProbability(1)
                .build();

        Network network = new Network(maxNeuronSize);

        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));
//        network.addListener(new LayoutAdapter(new GraphStreamDynamicLayout()));

        for (int idx = 0; idx < patternGenerator.getSize(); idx++) {
            int channelIdx = idx;
            network.addAdaptiveDictReceptor(() -> inputArray[channelIdx], Set.of(0));
        }

        for (int[] ints : patternGenerator) {
            inputArray = ints;
            System.out.println(Arrays.toString(inputArray));
            network.tick();
            Thread.sleep(300);
        }
    }
}
