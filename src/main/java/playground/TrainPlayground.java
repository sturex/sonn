package playground;

import neural.Network;
import neural.NetworkEventsListener;
import util.patgen.Pattern;
import util.patgen.PatternGenerator;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TrainPlayground {

    private static final Random random = new Random();
    public static final int bound = 2;

    public static final int patternSize = 1;
    public static final int patternLength = 200;
    public static final int maxNeuronSize = 200;
    private static int idx;
    private static int inputValue = 0;
    private static final BufferQueue<Integer> buf = new BufferQueue<>(3);
    private static boolean recognized = false;
    private static boolean trainCondition = false;
    private static boolean pain = false;


    public static void main(String[] args) throws InterruptedException {

        Pattern pattern = Pattern.randomized("p1", patternSize, patternLength, bound);

        PatternGenerator patternGenerator = PatternGenerator.newBuilder(patternSize)
                .addPattern(pattern, 1, 0.0)
                .withProbability(1)
                .build();

//        List<NetworkEventsListener> listeners = List.of(new LayoutAdapter(new GraphStreamStaticLayout()));
        List<NetworkEventsListener> listeners = List.of(new LayoutAdapter(new GraphStreamStaticLayout()),
                new LayoutAdapter(new GraphStreamDynamicLayout()));

        Network network = new Network(listeners, maxNeuronSize);

        network.addReceptor(() -> inputValue == 0);
        network.addReflex(() -> trainCondition, TrainPlayground::response);
        network.addPainReceptor(() -> pain);

        for (int[] ints : patternGenerator) {
            recognized = false;
            inputValue = ints[0];
            buf.addLast(inputValue);
            trainCondition = trainCondition();
            System.out.print(idx++ + ": " + inputValue + " - " + Arrays.toString(buf.toArray()));
            network.tick();
            pain = pain();
            System.out.println();
            Thread.sleep(200);
        }

    }

    private static boolean pain() {
        boolean pain = !trainCondition && recognized;
        if (pain){
            System.out.print(" - ");
        }
        return pain;
    }

    private static void response() {
        System.out.print(" * ");
        recognized = true;
    }

    private static boolean trainCondition() {
        List<Integer> lastValues = buf.stream().toList();
        if (lastValues.size() == 3) {
            return lastValues.get(0) == 0 && lastValues.get(1) == 1 && lastValues.get(2) == 1;
        }
        return false;
    }

    private static class BufferQueue<T> extends ArrayDeque<T> {

        private final int length;

        public BufferQueue(int length) {
            super(length);
            this.length = length;
        }

        @Override
        public void addLast(T t) {
            if (size() >= length) {
                pollFirst();
            }
            super.addLast(t);
        }
    }
}
