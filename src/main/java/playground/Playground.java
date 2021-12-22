package playground;

import neural.Effector;
import neural.Network;
import neural.Receptor;

public class Playground {

    public static void main(String[] args) {

        Network network = new Network();

        Receptor receptor = new Receptor(() -> true);
        Effector effector = new Effector(() -> {});

        network.addInput(receptor);
        network.addOutput(effector);

        network.triggerRelease();
    }

}
