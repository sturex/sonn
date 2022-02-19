package neural;

import core.Flow;
import core.Node;

import java.util.List;

public class PainEffector extends Node<Synapse<Neuron, PainEffector>, Action> {

    private final Network network;

    public PainEffector(Network network) {
        super(network, network.getNodesCount());
        this.network = network;
        addOutput(new Action(() ->
        {
            network.applyPainMode();
//            network.streamOfEffectors().forEach(e -> e.streamOfOutputs().forEach(a -> a.setPunished(getForwardFlow().toBoolean())));
        }
        ));
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
        setForwardFlow(Flow.STILL);
        streamOfInputs().forEach(i -> i.setForwardFlow(Flow.STILL));
    }

    @Override
    protected void onDeadendFound() {
        setForwardFlow(Flow.STILL);
        streamOfInputs().forEach(i -> i.setForwardFlow(Flow.STILL));
    }
}
