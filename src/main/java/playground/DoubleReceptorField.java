package playground;

import neural.Bounds;
import neural.Network;
import neural.NetworkEventsListener;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.List;
import java.util.Random;

public class DoubleReceptorField {

    private static final Random random = new Random();
    private static double d = 0.;
    private static final int lowerBound = 0;
    private static final int upperBound = 1;
    private static final int bCount = 3;
    private static final int maxNeuronSize = 100;

    public static void main(String[] args) throws InterruptedException {

        List<NetworkEventsListener> listeners = List.of(
                new LayoutAdapter(new GraphStreamStaticLayout()),
                new LayoutAdapter(new GraphStreamDynamicLayout()));

        Network network = new Network(listeners, maxNeuronSize);

        network.addDoubleReception(() -> d, new Bounds(lowerBound, upperBound), bCount);

        while (true) {
            d = random.nextDouble();
            network.tick();
            System.out.println(d);
            Thread.sleep(100);
        }
    }

}
