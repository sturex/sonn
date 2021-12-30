package vis;

import core.Node;
import neural.*;

public class LayoutAdapter implements NetworkEventsListener {

    private final NetworkLayout layout;

    public LayoutAdapter(NetworkLayout layout) {
        this.layout = layout;
    }

    @Override
    public void onReceptorStateChanged(Receptor receptor) {
        layout.updateInputNode(LayoutInputNode.of(receptor), receptor.isForwardRun());
    }

    @Override
    public void onEffectorStateChanged(Effector effector) {
        layout.updateOutputNode(LayoutOutputNode.of(effector), effector.isForwardRun());
    }

    @Override
    public void onNeuronStateChanged(Neuron neuron) {
        layout.updateInputNode(LayoutInputNode.of(neuron), neuron.isForwardRun());
        layout.updateOutputNode(LayoutOutputNode.of(neuron), neuron.isForwardRun());
    }

    @Override
    public void onSynapseStateChanged(Synapse<?, ?> synapse) {
        Node<?, ?> output = synapse.getOutput();
        Node<?, ?> input = synapse.getInput();
        layout.updateInnerNode(LayoutInnerNode.of(input, output), synapse.isForwardRun(), synapse.getType() == Synapse.Type.EXCITATORY);
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
        layout.addInnerNode(LayoutInnerNode.of(input, output), synapse.getType() == Synapse.Type.EXCITATORY);
    }
}
