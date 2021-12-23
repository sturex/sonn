package neural;

import core.Flow;
import core.FlowSupplier;
import core.Node;

import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

class Receptor extends Node<FlowSupplier, Synapse<Receptor, Neuron>> {

    private final Network network;

    Receptor(Network network, BooleanSupplier booleanSupplier) {
        this.network = network;
        addInput(FlowSupplier.of(booleanSupplier));
    }

    @Override
    public Flow converge(Stream<FlowSupplier> stream) {
        return Flow.converge(stream);
    }

    @Override
    public void backpass(Stream<Synapse<Receptor, Neuron>> stream) {

    }
}
