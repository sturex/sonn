package playground;

import neural.Network;

public class Playground {

    public static void main(String[] args) {

        Network network = new Network();

        network.addReceptor(() -> true);
        network.addEffector(() -> {});

        network.tick();

    }

}
