package neural;

import core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public class Network {

    private final List<Receptor> receptors = new ArrayList<>();
    private final List<Effector> effectors = new ArrayList<>();
    private final List<Neuron> neurons = new ArrayList<>();
    private final List<NetworkEventsListener> listeners = new ArrayList<>();

    static Flow convergeForward(List<? extends Synapse<?, ?>> synapses) {
        if (synapses.stream().anyMatch(s -> s.getForward() == Flow.RUN && s.getType() == Synapse.Type.INHIBITORY)) {
            return Flow.STILL;
        } else if (synapses.stream().anyMatch(s -> s.getForward() == Flow.RUN && s.getType() == Synapse.Type.EXCITATORY)) {
            return Flow.RUN;
        } else {
            return Flow.STILL;
        }
    }

    static Flow convergeBackward(List<? extends Synapse<?, ?>> synapses) {
        if (synapses.stream().anyMatch(s -> s.getBackward() == Flow.RUN && s.getType() == Synapse.Type.EXCITATORY)) {
            return Flow.STILL;
        } else if (synapses.stream().anyMatch(s -> s.getBackward() == Flow.RUN && s.getType() == Synapse.Type.EXCITATORY)) {
            return Flow.RUN;
        } else {
            return Flow.STILL;
        }
    }

    public void addListener(NetworkEventsListener listener) {
        listeners.add(Objects.requireNonNull(listener));
    }

    public Receptor addReceptor(BooleanSupplier booleanSupplier) {
        Receptor receptor = new Receptor(Objects.requireNonNull(booleanSupplier));
        receptors.add(receptor);
        listeners.forEach(l -> l.onReceptorAdded(receptor));
        return receptor;
    }

    public Effector addEffector(Runnable runnable) {
        Effector effector = new Effector(this, Objects.requireNonNull(runnable));
        effectors.add(effector);
        listeners.forEach(l -> l.onEffectorAdded(effector));
        return effector;
    }

    public void addReflex(BooleanSupplier booleanSupplier, Runnable runnable) {
        Receptor receptor = addReceptor(booleanSupplier);
        Effector effector = addEffector(runnable);
        connect(receptor, effector, Synapse.Type.EXCITATORY);
    }

    public void tick() {
        forwardPass();
        backwardPass();
        notifyListeners();
        createNewConnections();
    }

    private void notifyListeners() {
        listeners.forEach(l -> {
            for (Receptor receptor : receptors) {
                l.onNodeStateChanged(receptor);
                receptor.streamOfOutputs().forEach(l::onSynapseStateChanged);
            }
            for (Neuron neuron : neurons) {
                l.onNodeStateChanged(neuron);
                neuron.streamOfOutputs().forEach(l::onSynapseStateChanged);
            }
            effectors.forEach(l::onNodeStateChanged);
        });
    }

    private Neuron addNeuron() {
        Neuron neuron = new Neuron(this);
        neurons.add(neuron);
        listeners.forEach(l -> l.onNeuronAdded(neuron));
        return neuron;
    }

    private Optional<Neuron> requestTargetNeuron() {
        if (true) {
            return Optional.of(addNeuron());
        } else {
            return Optional.empty();
        }
    }

    private void backwardPass() {
        effectors.forEach(Node::triggerBackpass);
    }

    private void forwardPass() {
        receptors.forEach(Node::triggerConverge);
    }

    private void createNewConnections() {
        requestTargetNeuron().ifPresent(neuron -> {
            streamOfDeadendNodes().forEach(node -> connect(node, neuron, Synapse.Type.EXCITATORY));
            streamOfSidewayNodes().forEach(node -> connect(node, neuron, Synapse.Type.INHIBITORY));
        });
    }

    private void connect(Node source, Node target, Synapse.Type type) {
        Synapse synapse = new Synapse<>(source, target, type);
        source.addOutput(synapse);
        target.addInput(synapse);
        listeners.forEach(l -> l.onSynapseAdded(synapse));
    }

    private Stream<Node<?, ?>> streamOfDeadendNodes() {
        return Stream.of(receptors.stream().filter(Node::isDeadend), neurons.stream().filter(Node::isDeadend)).flatMap(i -> i);
    }

    private Stream<Node<?, ?>> streamOfRunEffectors() {
        return Stream.of(effectors.stream().filter(Node::isRun)).flatMap(i -> i);
    }

    private Stream<Node<?, ?>> streamOfSidewayNodes() {
        return Stream.of(receptors.stream().filter(Node::isSideway), neurons.stream().filter(Node::isSideway)).flatMap(i -> i);
    }
}
