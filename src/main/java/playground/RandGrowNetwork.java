package playground;

import neural.Network;
import neural.NetworkEventsListener;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;

public class RandGrowNetwork {

    private static final Random random = new Random();
    private static int i = 0;
    private static final BooleanSupplier bs = () -> i++ == 0 || random.nextInt(100) > 50;

    public static void main(String[] args) throws InterruptedException {

        List<NetworkEventsListener> listeners = List.of(
                new LayoutAdapter(new GraphStreamStaticLayout()),
                new LayoutAdapter(new GraphStreamDynamicLayout()));
        Network network = new Network(listeners, 100);

        network.addReceptor(bs);

        for (int idx = 0; idx < 200; idx++) {
            System.out.print(idx + ": ");
            network.tick();
            Thread.sleep(200);
        }
    }
}
