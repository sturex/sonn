package playground;

import neural.Network;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.Random;
import java.util.function.BooleanSupplier;

public class RandGrowNetwork {

    private static final Random random = new Random();
    private static final BooleanSupplier bs = () -> random.nextInt(100) > 90;

    public static void main(String[] args) throws InterruptedException {

        Network network = new Network(700);

        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));

        network.addReceptor(bs);
        network.addReceptor(bs);
        network.addReceptor(bs);
        network.addReceptor(bs);

        for (int idx = 0; idx < 2000; idx++) {
            network.tick();
            Thread.sleep(100);
        }
    }
}
