package neural;

import core.Flow;
import core.Node;

import java.util.List;

public class Neuron extends Node<Synapse<Node<?, ?>, Node<?, ?>>, Synapse<Node<?, ?>, Node<?, ?>>> {

    private boolean justCreated = true;

    @Override
    public Flow convergeForward(List<Synapse<Node<?, ?>, Node<?, ?>>> ts) {
        return Network.convergeForward(ts);
    }

    @Override
    public Flow convergeBackward(List<Synapse<Node<?, ?>, Node<?, ?>>> us) {
        if (justCreated) {
            justCreated = false;
            return Flow.RUN;
        } else {
            return Network.convergeBackward(us);
        }
    }

    @Override
    public String toString() {
        return "N" + super.toString();
    }
}