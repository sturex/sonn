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
    private static final BooleanSupplier bs = () -> random.nextInt(100) > 40;
    private static final BooleanSupplier bs2 = () -> random.nextInt(100) > 90;
//    private static final BooleanSupplier bs = () -> i++ == 0 || random.nextInt(100) > 50;

    public static void main(String[] args) throws InterruptedException {

        List<NetworkEventsListener> listeners = List.of(
                new LayoutAdapter(new GraphStreamStaticLayout()),
                new LayoutAdapter(new GraphStreamDynamicLayout()));

        Network network = new Network(listeners, 200);

        network.addReceptor(bs);
        network.addReceptor(bs);
        network.addReceptor(bs);
        network.addReceptor(bs);
        network.addReceptor(bs);
//        network.addReflex(bs, () -> System.out.println("*"));
        network.addPainReceptor(bs2);

        for (int idx = 0; idx < 20000; idx++) {
            System.out.println(idx + ": " + network.getNodesCount());
            network.tick();
            Thread.sleep(50);
        }
    }
}
