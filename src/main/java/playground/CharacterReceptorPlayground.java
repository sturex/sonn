package playground;

import neural.Network;
import neural.NetworkEventsListener;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CharacterReceptorPlayground {

    private static final int maxNeuronSize = 1000;
    private static Character ch;
    private final static Random random = new Random();

    public static void main(String[] args) throws InterruptedException, IOException {
        String sourceText = "This test will be written in network structure";
        //Util.readAsString("data/lorem_ipsum.txt");

        List<NetworkEventsListener> listeners = List.of(
                new LayoutAdapter(new GraphStreamStaticLayout()),
                new LayoutAdapter(new GraphStreamDynamicLayout()));

        Network network = new Network(listeners, maxNeuronSize);

        network.addAdaptiveDictReceptor(() -> ch, Set.of(' '));

        int idx = 0;
        for (char cdx : sourceText.toCharArray()) {
            ch = cdx;
            network.tick();
            System.out.println(idx + ": " + ch + " ");
            Thread.sleep(300);
        }
    }
}
