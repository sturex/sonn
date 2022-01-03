package playground;

import neural.Network;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.Random;

public class RandGrowNetwork {

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        Network network = new Network();

        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));

        network.addReceptor(random::nextBoolean);
        network.addReceptor(random::nextBoolean);
        network.addReceptor(random::nextBoolean);
        network.addReceptor(random::nextBoolean);

        for (int idx = 0; idx < 20; idx++) {
            network.tick();
            Thread.sleep(50);
        }
    }
}
