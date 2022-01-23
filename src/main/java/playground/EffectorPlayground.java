package playground;

import neural.Network;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.Random;

public class EffectorPlayground {

    public static final int maxNeuronSize = 100;
    private static final Random random = new Random();
    public static Boolean b;
    static int idx = 0;

    public static void main(String[] args) throws InterruptedException {
        Network network = new Network(maxNeuronSize);

        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));
        network.addListener(new LayoutAdapter(new GraphStreamDynamicLayout()));

        network.addReflex(() -> b, () -> System.out.println(idx + ": " + b));

        for (; idx < 1000; idx++) {
            b = random.nextBoolean();
            network.tick();
            Thread.sleep(200);
        }

    }

}
