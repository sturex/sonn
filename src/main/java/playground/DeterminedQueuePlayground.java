package playground;

import neural.Network;
import neural.NetworkEventsListener;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class DeterminedQueuePlayground {

    static int idx = 0;

    public static void main(String[] args) throws InterruptedException {

        List<NetworkEventsListener> listeners = List.of(
                new LayoutAdapter(new GraphStreamStaticLayout()),
                new LayoutAdapter(new GraphStreamDynamicLayout()));

        Network network = new Network(listeners, 100);

        Queue<Integer> rQueue = new ArrayDeque<>();
        Queue<Integer> hQueue = new ArrayDeque<>();
        rQueue.add(1); //1
        rQueue.add(0);//2
        rQueue.add(1); //3
        rQueue.add(1); //4
        rQueue.add(0);//5
        rQueue.add(1); //6
        rQueue.add(0);//7
        rQueue.add(1); //8

        hQueue.add(1); //1
        hQueue.add(0);//2
        hQueue.add(0); //3
        hQueue.add(0); //4
        hQueue.add(1);//5
        hQueue.add(0); //6
        hQueue.add(0);//7
        hQueue.add(0); //8

        Random random = new Random();

        network.addReflex(() -> {
            if (!rQueue.isEmpty()) {
                return rQueue.poll() != 0;
            } else return random.nextBoolean();
        }, () -> System.out.println(idx));
//        network.addReceptor(() -> rQueue.poll() != 0);
        network.addPainReceptor(() -> {
            if (!hQueue.isEmpty()) {
                return hQueue.poll() != 0;
            } else return false;
        });

        while (true) {
            System.out.println("idx: " + idx++);
            network.tick();
            Thread.sleep(200);
        }
    }

}
