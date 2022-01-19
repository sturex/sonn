package playground;

import neural.Network;
import vis.GraphStreamDynamicLayout;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

public class CharacterReceptorPlayground {

    private static final int maxNeuronSize = 1000;
    private static Character ch;
    private final static Random random = new Random();

    public static void main(String[] args) throws InterruptedException, IOException {
        String sourceText = "This test will be written in network structure";
        //Util.readAsString("data/lorem_ipsum.txt");

        Network network = new Network(maxNeuronSize);

        network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));
        network.addListener(new LayoutAdapter(new GraphStreamDynamicLayout()));

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
