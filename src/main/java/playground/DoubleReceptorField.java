package playground;

import neural.Network;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.Random;

public class DoubleReceptorField {

    record Bounds(double lowerBound, double upperBound) {
    }

    private static final Random random = new Random();
    private static double d = 0.;
    private static final int lowerBound = -100;
    private static final int upperBound = 100;
    private static final int bCount = 3;
    private static final int maxNeuronSize = 100;

    public static void main(String[] args) throws InterruptedException {
        Network network = new Network(maxNeuronSize);

        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));
        network.addListener(new LayoutAdapter(new GraphStreamDynamicLayout()));

        network.addReceptorField(() -> d,
                new Bounds(lowerBound, upperBound),
                bCount,
                (value, bounds) -> (int) ((value - bounds.lowerBound) / ((bounds.upperBound - bounds.lowerBound) / bCount)));

        while (true) {
            network.tick();
            d = lowerBound + random.nextDouble() * (upperBound - lowerBound);
            System.out.println(d);
            Thread.sleep(300);
        }
    }

}
