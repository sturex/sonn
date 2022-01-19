package neural;

import core.Flow;
import core.Node;

import java.util.List;

public class Neuron extends Node<Synapse<Node<?, ?>, Node<?, ?>>, Synapse<Node<?, ?>, Node<?, ?>>> {

    public Neuron(Network network) {
        super(network, network.getNodesCount());
    }

    @Override
    public Flow convergeForward(List<Synapse<Node<?, ?>, Node<?, ?>>> ts) {
        return Network.convergeForward(ts);
    }

    @Override
    public Flow convergeBackward(List<Synapse<Node<?, ?>, Node<?, ?>>> us) {
        return Network.convergeBackward(us);
    }

    @Override
    public String toString() {
        return "N" + super.toString();
    }

    public void resetState() {
        streamOfOutputs().forEach(Synapse::resetState);
    }
}