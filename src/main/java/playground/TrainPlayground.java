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
import java.util.function.BooleanSupplier;

public class TrainPlayground {

    private static final Random random = new Random();
    public static final int bound = 2;

    public static final int patternSize = 1;
    public static final int patternLength = 100;
    public static final int maxNeuronSize = 20;
    private static int idx;
    private static int inputValue = 0;
    private static final BufferQueue<Integer> buf = new BufferQueue<>(3);
    private static boolean recognized = false;
    private static boolean patternMatched = false;


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
        network.addReflex(TrainPlayground::trainCondition, TrainPlayground::response);
        network.addPainReceptor(pain());

        for (int[] ints : patternGenerator) {
            recognized = false;
            inputValue = ints[0];
            buf.addLast(inputValue);
            System.out.print(idx++ + ": " + inputValue + " - " + Arrays.toString(buf.toArray()));
            network.tick();
            System.out.println();
            Thread.sleep(200);
        }

    }

    private static BooleanSupplier pain() {
        return () -> {
            boolean b = !patternMatched && recognized;
//            recognized = false;
            return b;
        };
    }

    private static void response() {
        System.out.println("*");
        recognized = true;
    }

    private static boolean trainCondition() {
        List<Integer> lastValues = buf.stream().toList();
        if (lastValues.size() == 3) {
            patternMatched = lastValues.get(0) == 0 && lastValues.get(1) == 1 && lastValues.get(2) == 1;
            return patternMatched;
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
