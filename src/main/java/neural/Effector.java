package neural;

import core.Flow;
import core.Node;

import java.util.List;

public class Effector extends Node<Synapse<Neuron, Effector>, Action> {

    private final Network network;

    Effector(Network network, Runnable runnable) {
        super(network, network.getNodesCount());
        this.network = network;
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

    @Override
    protected void onRunFound() {
        network.onRunEffectorFound(this);
        setForwardFlow(Flow.STILL);
        streamOfInputs().forEach(i -> i.setForwardFlow(Flow.STILL));
    }


    @Override
    protected void onDeadendFound() {
        network.onPunishedEffectorFound(this);
        setForwardFlow(Flow.STILL);
        streamOfInputs().forEach(i -> i.setForwardFlow(Flow.STILL));
    }
}
