package playground;

import core.Node;
import neural.Network;
import neural.NetworkEventsListener;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;
import vis.NetworkLayout;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class Playground {

    public static void main(String[] args) throws InterruptedException {

        Network network = new Network();

        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));

        Queue<Boolean> queue = new ArrayDeque<>();
        queue.add(true);
        queue.add(false);
        queue.add(true);
        queue.add(true);
        queue.add(false);
        queue.add(true);

        network.addReceptor(queue::poll);

        while (!queue.isEmpty()){
            network.tick();
            Thread.sleep(200);
        }
    }

}
