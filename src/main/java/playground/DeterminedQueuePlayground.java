package playground;

import neural.Network;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.ArrayDeque;
import java.util.Queue;

public class DeterminedQueuePlayground {

    public static void main(String[] args) throws InterruptedException {

        Network network = new Network(50);

        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));
        network.addListener(new LayoutAdapter(new GraphStreamDynamicLayout()));

        Queue<Boolean> queue = new ArrayDeque<>();
        queue.add(true); //1
        queue.add(false);//2
        queue.add(true); //3
        queue.add(true); //4
        queue.add(false);//5
        queue.add(true); //6
        queue.add(false);//7
        queue.add(true); //8

        network.addReceptor(() -> {
            boolean poll = queue.poll();
            queue.add(poll);
            return poll;
        });

        while (true){
            network.tick();
            Thread.sleep(200);
        }
    }

}
