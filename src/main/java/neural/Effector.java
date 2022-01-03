package neural;

import core.Flow;
import core.Node;

import java.util.List;

public class Effector extends Node<Synapse<Neuron, Effector>, Action> {

    Effector(Runnable runnable) {
        addOutput(new Action(runnable));
    }

    @Override
    public Flow convergeForward(List<Synapse<Neuron, Effector>> ts) {
        return Network.convergeForward(ts);
    }

    @Override
    public Flow convergeBackward(List<Action> us) {
        return Flow.convergeBackward(us);
    }

    @Override
    public String toString() {
        return "E" + super.toString();
    }
}
