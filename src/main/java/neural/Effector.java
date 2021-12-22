package neural;

import core.Flow;
import core.FlowConsumer;
import core.Node;

import java.util.stream.Stream;

public class Effector extends Node<Synapse<Neuron, Effector>, FlowConsumer> implements FlowConsumer {

    private final Runnable runnable;

    public Effector(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void accept(Flow flow) {
        flow.start(runnable);
    }

    @Override
    public void release(Stream<Synapse<Neuron, Effector>> stream) {

    }

    @Override
    public void backpass(Stream<FlowConsumer> stream) {

    }
}
