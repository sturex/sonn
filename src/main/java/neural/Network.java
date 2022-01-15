package neural;

import core.Flow;
import core.Graph;
import core.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.*;

public class Network implements Graph {

    private final List<Receptor> receptors = new ArrayList<>();
    private final List<Effector> effectors = new ArrayList<>();
    private final List<Neuron> neurons = new ArrayList<>();
    private final List<Node<?, ?>> deadendNodes = new ArrayList<>();
    private final List<Node<?, ?>> sidewayNodes = new ArrayList<>();
    private final List<Node<?, ?>> leafNodes = new ArrayList<>();
    private final List<NetworkEventsListener> listeners = new ArrayList<>();
    private int timestamp = 0;
    Neuron targetNeuron = null;
    private final int maxNeuronSize;

    public Network(int maxNeuronSize) {
        this.maxNeuronSize = maxNeuronSize;
    }

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

    public <T> void addReceptor(T t, Function<T, BooleanSupplier> transform) {
        addReceptor(transform.apply(t));
    }

    public <T, U> void addReceptorField(Supplier<T> tSupplier, U u, int receptorCount, BiFunction<T, U, Integer> receptorMapper) {
        for (int i = 0; i < receptorCount; i++) {
            int bdx = i;
            addReceptor(tSupplier, t1 -> () -> {
                int bucketIndex = receptorMapper.apply(tSupplier.get(), u);
                bucketIndex = bucketIndex < 0 ? 0 : Math.min(bucketIndex, receptorCount - 1);
                return bucketIndex == bdx;
            });
        }
    }

    public void addDoubleReception(DoubleSupplier doubleSupplier, Bounds bounds, int receptorCount){
        addReceptorField(doubleSupplier::getAsDouble,
                bounds,
                receptorCount,
                (value, bounds1) -> (int) ((value - bounds1.lowerBound()) / ((bounds1.upperBound() - bounds1.lowerBound()) / receptorCount)));
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void increaseTimestamp() {
        timestamp++;
    }

    public void addListener(NetworkEventsListener listener) {
        listeners.add(Objects.requireNonNull(listener));
    }

    public Receptor addReceptor(BooleanSupplier booleanSupplier) {
        Receptor receptor = new Receptor(this, Objects.requireNonNull(booleanSupplier));
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

    private Neuron addNeuron() {
        Neuron neuron = new Neuron(this);
        neurons.add(neuron);
        listeners.forEach(l -> l.onNeuronAdded(neuron));
        return neuron;
    }

    public void tick() {
        forwardPass();
        notifyListeners();
        backwardPass();
        listeners.forEach(l -> l.onDeadendNodesDetected(deadendNodes));
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

    private void backwardPass() {
        effectors.forEach(Node::triggerBackpass);
        leafNodes.forEach(Node::triggerBackpass);
        leafNodes.clear();
    }

    private void forwardPass() {
        receptors.forEach(Node::triggerConverge);
    }

    private void createNewConnections() {
        if (neurons.size() < maxNeuronSize) {
            targetNeuron = addNeuron();
            deadendNodes.forEach(d -> connect(d, targetNeuron, Synapse.Type.EXCITATORY));
            sidewayNodes.forEach(d -> connect(d, targetNeuron, Synapse.Type.INHIBITORY));
        }
        deadendNodes.clear();
        sidewayNodes.clear();
    }

    private void connect(Node source, Node target, Synapse.Type type) {
        assert !source.equals(target);
        Synapse synapse = new Synapse<>(source, target, type);
        source.addOutput(synapse);
        target.addInput(synapse);
        listeners.forEach(l -> l.onSynapseAdded(synapse));
    }

    @Override
    public void onDeadendNodeFound(Node<?, ?> node) {
        assert !deadendNodes.contains(node) : timestamp + ": " + node.toString();
        //TODO ugly non-optimal workaround solution
        if (node instanceof Neuron neuron) {
            neuron.streamOfInputs().forEach(synapse -> {
                if (synapse.getType() == Synapse.Type.EXCITATORY) {
                    synapse.getInput().setParent();
                }
            });
        }
        deadendNodes.add(node);
    }

    @Override
    public void onSidewayNodeFound(Node<?, ?> node) {
        assert !sidewayNodes.contains(node) : timestamp + ": " + node.toString();
        sidewayNodes.add(node);
    }

    @Override
    public void onLeafNodeFound(Node<?, ?> node) {
        leafNodes.add(node);
    }
}
