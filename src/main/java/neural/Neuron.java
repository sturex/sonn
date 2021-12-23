package neural;

import core.Flow;
import core.Node;

import java.util.stream.Stream;

class Neuron extends Node<Synapse<Neuron, Neuron>, Synapse<Neuron, Neuron>> {

    private final Network network;

    Neuron(Network network) {
        this.network = network;
    }

    @Override
    public void backpass(Stream<Synapse<Neuron, Neuron>> stream) {

    }

    @Override
    public Flow converge(Stream<Synapse<Neuron, Neuron>> stream) {
        return Network.converge(stream);
    }
}