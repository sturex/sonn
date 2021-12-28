package neural;

public interface NetworkEventsListener {


    void onReceptorAdded(Receptor receptor);

    void onEffectorAdded(Effector effector);

    void onNeuronAdded(Neuron neuron);

    void onSynapseAdded(Synapse<?, ?> synapse);
}
