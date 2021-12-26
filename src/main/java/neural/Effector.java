package neural;

import core.Flow;
import core.Node;

import java.util.stream.Stream;

class Effector extends Node<Synapse<Neuron, Effector>, Action> {

    Effector(Network network, Runnable runnable) {
        super(network);
        addOutput(new Action(runnable));
    }

    @Override
    public Flow convergeForward(Stream<Synapse<Neuron, Effector>> stream) {
        return Network.convergeForward(stream);
    }

    @Override
    public Flow convergeBackward(Stream<Action> stream) {
        return Flow.convergeBackward(stream);
    }
}
