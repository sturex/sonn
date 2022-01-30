package playground;

import neural.Network;
import neural.NetworkEventsListener;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.List;
import java.util.Random;

public class PainPlayground {

    static int idx = 0;

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        List<NetworkEventsListener> listeners = List.of(
                new LayoutAdapter(new GraphStreamStaticLayout()),
                new LayoutAdapter(new GraphStreamDynamicLayout()));
        Network network = new Network(listeners, 400);

        network.addReflex(() -> random.nextInt(100) > 55, () -> System.out.println(idx++));
        network.addReceptor(() -> random.nextInt(100) > 95);
        network.addReceptor(() -> random.nextInt(100) > 95);
        network.addPainReceptor(() -> random.nextInt(100) > 90);

        while (true) {
            System.out.println("--------------------------------------------------------------------------------");
            network.tick();
            Thread.sleep(200);
        }
    }
}
