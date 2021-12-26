package neural;

import core.Flow;
import core.Node;

import java.util.stream.Stream;

class Neuron extends Node<Synapse<Neuron, Neuron>, Synapse<Neuron, Neuron>> {

    Neuron(Network network) {
        super(network);
    }

    @Override
    public Flow convergeBackward(Stream<Synapse<Neuron, Neuron>> stream) {
        return Network.convergeBackward(stream);
    }

    @Override
    public Flow convergeForward(Stream<Synapse<Neuron, Neuron>> stream) {
        return Network.convergeForward(stream);
    }
}