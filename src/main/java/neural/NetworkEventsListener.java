package neural;

import core.Node;

public interface NetworkEventsListener {

    void onNodeStateChanged(Node<?, ?> node);

    void onReceptorAdded(Receptor receptor);

    void onEffectorAdded(Effector effector);

    void onNeuronAdded(Neuron neuron);

    void onSynapseAdded(Synapse<?, ?> synapse);
}
