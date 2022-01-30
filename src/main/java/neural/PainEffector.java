package neural;

import core.Flow;
import core.Node;

import java.util.List;

public class PainEffector extends Node<Synapse<Neuron, PainEffector>, Action> {

    public PainEffector(Network network) {
        super(network, network.getNodesCount());
        addOutput(new Action(() -> network.streamOfEffectors()
                .filter(Node::isForwardRun)
                .forEach(e -> e.streamOfOutputs()
                        .forEach(a -> a.setBackwardFlow(Flow.STILL)))));
    }

    @Override
    public Flow convergeForward(List<Synapse<Neuron, PainEffector>> ts) {
        return Network.convergeForward(ts);
    }

    @Override
    public Flow convergeBackward(List<Action> us) {
        return Flow.convergeBackward(us);
    }

    @Override
    public String toString() {
        return "HE" + super.toString();
    }

    @Override
    protected void onRunFound() {

    }

    @Override
    protected void onDeadendFound() {

    }
}
