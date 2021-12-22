package neural;

import core.Node;

import java.util.stream.Stream;

public class Neuron extends Node<Synapse<Neuron, Neuron>, Synapse<Neuron, Neuron>> {

    @Override
    public void backpass(Stream<Synapse<Neuron, Neuron>> stream) {

    }

    @Override
    public void release(Stream<Synapse<Neuron, Neuron>> stream) {

    }
}
