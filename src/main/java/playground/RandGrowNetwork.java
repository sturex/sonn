package playground;

import neural.Network;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.Random;
import java.util.function.BooleanSupplier;

public class RandGrowNetwork {

    private static final Random random = new Random();
    private static int i = 0;
    private static final BooleanSupplier bs = () -> i++ == 0 || random.nextInt(100) > 96;

    public static void main(String[] args) throws InterruptedException {

        Network network = new Network(100);

        network.addListener(new LayoutAdapter(new GraphStreamDynamicLayout()));
        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));

        network.addReceptor(bs);

        for (int idx = 0; idx < 200; idx++) {
            System.out.print(idx + ": ");
            network.tick();
            Thread.sleep(200);
        }
    }
}
