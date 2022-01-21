package vis;

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
        layout.updateInnerNode(LayoutEdge.of(synapse));
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
        layout.addInnerNode(LayoutInnerNode.of(neuron));
    }

    @Override
    public void onSynapseAdded(Synapse<?, ?> synapse) {
        layout.addEdge(LayoutEdge.of(synapse));
    }
}
