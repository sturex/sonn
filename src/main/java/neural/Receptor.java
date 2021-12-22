package neural;

import core.Flow;
import core.FlowSupplier;
import core.Node;

import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public class Receptor extends Node<FlowSupplier, Synapse<Receptor, Neuron>> implements FlowSupplier {

    private final BooleanSupplier booleanSupplier;

    public Receptor(BooleanSupplier booleanSupplier) {
        this.booleanSupplier = booleanSupplier;
    }

    @Override
    public Flow get() {
        return Flow.from(booleanSupplier);
    }

    @Override
    public void release(Stream<FlowSupplier> stream) {

    }

    @Override
    public void backpass(Stream<Synapse<Receptor, Neuron>> stream) {

    }
}
