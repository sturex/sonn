package playground;

import core.Node;
import neural.Network;
import neural.NetworkEventsListener;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;
import vis.NetworkLayout;

import java.util.Random;

public class Playground {

    public static void main(String[] args) throws InterruptedException {

        Network network = new Network();

        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));

        Random random = new Random();

        network.addReceptor(random::nextBoolean);
        network.addReceptor(random::nextBoolean);
        network.addReceptor(random::nextBoolean);
        network.addReceptor(random::nextBoolean);
//
//        network.addReflex(random::nextBoolean, () -> System.out.print(1));
//        network.addReflex(random::nextBoolean, () -> System.out.print(2));
//        network.addReflex(random::nextBoolean, () -> System.out.print(3));
//        network.addReflex(random::nextBoolean, () -> System.out.print(4));

        for (int idx = 0; idx < 200; idx++) {
            network.tick();
            Thread.sleep(200);
        }
    }

}
