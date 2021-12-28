package vis;

import core.Node;
import neural.*;

public class LayoutAdapter implements NetworkEventsListener {

    private final NetworkLayout layout;

    public LayoutAdapter(NetworkLayout layout) {
        this.layout = layout;
    }

    @Override
    public void onNodeStateChanged(Node<?, ?> node) {
        layout.updateNode(node.getId(), node.isForwardRun());
    }

    @Override
    public void onReceptorAdded(Receptor receptor) {
        layout.addInputNode(LayoutInputNode.of(receptor));
    }

    @Override
    public void onEffectorAdded(Effector effector) {
        layout.addOutputNode(LayoutOutputNode.of(effector));
    }

    @Override
    public void onNeuronAdded(Neuron neuron) {
        layout.addInputNode(LayoutInputNode.of(neuron));
        layout.addOutputNode(LayoutOutputNode.of(neuron));
    }

    @Override
    public void onSynapseAdded(Synapse<?, ?> synapse) {
        Node<?, ?> output = synapse.getOutput();
        Node<?, ?> input = synapse.getInput();
        layout.addInnerNode(LayoutInnerNode.of(input, output));
    }
}
