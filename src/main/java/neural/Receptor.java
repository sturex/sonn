package neural;

import core.Flow;
import core.Node;

import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

class Receptor extends Node<Reception, Synapse<Receptor, Neuron>> {

    Receptor(Network network, BooleanSupplier booleanSupplier) {
        super(network);
        addInput(new Reception(booleanSupplier));
    }

    @Override
    public Flow convergeForward(Stream<Reception> stream) {
        return Flow.convergeForward(stream);
    }

    @Override
    public Flow convergeBackward(Stream<Synapse<Receptor, Neuron>> stream) {
        return Network.convergeBackward(stream);
    }
}
