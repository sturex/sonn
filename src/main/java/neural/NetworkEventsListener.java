package neural;

import core.Node;

import java.util.List;

public interface NetworkEventsListener {

    default void onReceptorStateChanged(Receptor receptor){}

    default void onEffectorStateChanged(Effector effector){}

    default void onNeuronStateChanged(Neuron neuron){}

    default void onSynapseStateChanged(Synapse<?, ?> synapse){}

    default void onReceptorAdded(Receptor receptor){}

    default void onEffectorAdded(Effector effector){}

    default void onNeuronAdded(Neuron neuron){}

    default void onSynapseAdded(Synapse<?, ?> synapse){}

    default void onDeadendNodesDetected(List<Node<?, ?>> deadendNodes, int maxDeadendNeuronCount){}
}
