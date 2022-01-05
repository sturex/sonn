package neural;

import core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Network {

    private final List<Receptor> receptors = new ArrayList<>();
    private final List<Effector> effectors = new ArrayList<>();
    private final List<Neuron> neurons = new ArrayList<>();
    private final List<NetworkEventsListener> listeners = new ArrayList<>();
    private int timestamp = 0;
    Neuron targetNeuron = null;

    static Flow convergeForward(List<? extends Synapse<?, ?>> synapses) {
        assert !synapses.isEmpty();
        if (synapses.stream().anyMatch(s -> s.getForward() == Flow.RUN && s.getType() == Synapse.Type.INHIBITORY)) {
            return Flow.STILL;
        } else if (synapses.stream().anyMatch(s -> s.getForward() == Flow.RUN && s.getType() == Synapse.Type.EXCITATORY)) {
            return Flow.RUN;
        } else {
            return Flow.STILL;
        }
    }

    static Flow convergeBackward(List<? extends Synapse<?, ?>> synapses) {
        assert synapses.isEmpty() || synapses.stream().anyMatch(s -> s.getType() == Synapse.Type.EXCITATORY);
        if (synapses.stream().anyMatch(s -> s.getBackward() == Flow.RUN && s.getType() == Synapse.Type.EXCITATORY)) {
            return Flow.RUN;
        } else {
            return Flow.STILL;
        }
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void increaseTimestamp(){
        timestamp++;
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
        Effector effector = new Effector(Objects.requireNonNull(runnable));
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
        increaseTimestamp();
    }

    private void notifyListeners() {
        listeners.forEach(l -> {
            for (Receptor receptor : receptors) {
                l.onReceptorStateChanged(receptor);
                receptor.streamOfOutputs().forEach(l::onSynapseStateChanged);
            }
            for (Neuron neuron : neurons) {
                l.onNeuronStateChanged(neuron);
                neuron.streamOfOutputs().forEach(l::onSynapseStateChanged);
            }
            effectors.forEach(l::onEffectorStateChanged);
        });
    }

    private Neuron addNeuron() {
        Neuron neuron = new Neuron();
        neurons.add(neuron);
        listeners.forEach(l -> l.onNeuronAdded(neuron));
        return neuron;
    }

    private void backwardPass() {
        effectors.forEach(Node::triggerBackpass);
        Optional.ofNullable(targetNeuron).ifPresentOrElse(Node::triggerBackpass, () -> receptors.forEach(Node::triggerBackpass));
    }

    private void forwardPass() {
        receptors.forEach(Node::triggerConverge);
    }

    private void createNewConnections() {
        List<? extends Node<?, ?>> deadendNodes = findDeadendNodes();
        if (!deadendNodes.isEmpty()) {
            targetNeuron = addNeuron();
            List<? extends Node<?, ?>> sidewayNodes = findSidewayNodes();
            deadendNodes.forEach(d -> {
                Synapse synapse = connect(d, targetNeuron, Synapse.Type.EXCITATORY);
                listeners.forEach(l -> l.onSynapseAdded(synapse));
            });
            sidewayNodes.forEach(d -> {
                Synapse synapse = connect(d, targetNeuron, Synapse.Type.INHIBITORY);
                listeners.forEach(l -> l.onSynapseAdded(synapse));
            });
        }
    }

    public static Synapse connect(Node source, Node target, Synapse.Type type) {
        assert !source.equals(target);
        Synapse synapse = new Synapse<>(source, target, type);
        source.addOutput(synapse);
        target.addInput(synapse);
        return synapse;
    }

    private List<? extends Node<?, ?>> findDeadendNodes() {
        List<Node<?, ?>> nodes = Stream.of(receptors.stream().filter(Node::isDeadend), neurons.stream().filter(Node::isDeadend)).flatMap(i -> i).collect(Collectors.toList());
        Optional.ofNullable(targetNeuron).ifPresent(e -> {
            if (!nodes.contains(e)) {
                nodes.add(e);
            }
        });
//        Optional.ofNullable(targetNeuron).ifPresent(nodes::add);
        return nodes;
    }

    private List<Effector> findRunEffectors() {
        return Stream.of(effectors.stream().filter(Node::isRun)).flatMap(i -> i).collect(Collectors.toList());
    }

    private List<? extends Node<?, ?>> findSidewayNodes() {
        return Stream.of(receptors.stream().filter(Node::isSideway), neurons.stream().filter(Node::isSideway)).flatMap(i -> i).collect(Collectors.toList());
    }
}
