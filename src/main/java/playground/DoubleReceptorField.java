package playground;

import neural.Bounds;
import neural.Network;
import vis.GraphStreamDynamicLayout;
import vis.LayoutAdapter;

import java.util.Random;

public class DoubleReceptorField {

    private static final Random random = new Random();
    private static double d = 0.;
    private static final int lowerBound = 0;
    private static final int upperBound = 1;
    private static final int bCount = 3;
    private static final int maxNeuronSize = 100;

    public static void main(String[] args) throws InterruptedException {
        Network network = new Network(maxNeuronSize);

//        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));
        network.addListener(new LayoutAdapter(new GraphStreamDynamicLayout()));

        network.addDoubleReception(() -> d, new Bounds(lowerBound, upperBound), bCount);

        while (true) {
            d = random.nextDouble();
            network.tick();
            System.out.println(d);
            Thread.sleep(100);
        }
    }

}
