package neural;

import core.Node;

public interface NetworkEventsListener {

    default void onReceptorStateChanged(Receptor receptor){}

    default void onEffectorStateChanged(Effector effector){}

    default void onNeuronStateChanged(Neuron neuron){}

    default void onSynapseStateChanged(Synapse<?, ?> synapse){}

    default void onReceptorAdded(Receptor receptor){}

    default void onEffectorAdded(Effector effector){}

    default void onNeuronAdded(Neuron neuron){}

    default void onSynapseAdded(Synapse<?, ?> synapse){}
}
