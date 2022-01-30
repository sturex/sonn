package playground;

import neural.Network;
import neural.NetworkEventsListener;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class DeterminedQueuePlayground {

    static int idx = 0;
    public static void main(String[] args) throws InterruptedException {

        List<NetworkEventsListener> listeners = List.of(
                new LayoutAdapter(new GraphStreamStaticLayout()),
                new LayoutAdapter(new GraphStreamDynamicLayout()));

        Network network = new Network(listeners, 10);

        Queue<Boolean> queue = new ArrayDeque<>();
        queue.add(true); //1
        queue.add(false);//2
        queue.add(true); //3
        queue.add(true); //4
        queue.add(false);//5
        queue.add(true); //6
        queue.add(false);//7
        queue.add(true); //8

        network.addReflex(() -> {
            boolean poll = queue.poll();
            queue.add(poll);
            return poll;
        }, () -> System.out.println(idx++));

        while (true){
            System.out.print("tick: ");
            network.tick();
            Thread.sleep(200);
        }
    }

}
