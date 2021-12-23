package neural;

import core.Flow;
import core.FlowConsumer;
import core.Node;

import java.util.stream.Stream;

class Effector extends Node<Synapse<Neuron, Effector>, FlowConsumer> {

    private final Network network;

    Effector(Network network, Runnable runnable) {
        this.network = network;
        addOutput(flow -> flow.start(runnable));
    }

    @Override
    public Flow converge(Stream<Synapse<Neuron, Effector>> stream) {
        return Network.converge(stream);
    }

    @Override
    public void backpass(Stream<FlowConsumer> stream) {

    }
}
