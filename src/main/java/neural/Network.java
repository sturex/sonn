package neural;

import core.Flow;
import core.Graph;
import core.Node;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Network implements Graph {

    public static final int OUT_OF_RANGE_VALUE = -1;
    private final List<Receptor> receptors = new ArrayList<>();
    private final List<Receptor> addedReceptors = new ArrayList<>();
    private final List<Effector> effectors = new ArrayList<>();
    private final List<Effector> runEffectors = new ArrayList<>();
    private final List<Effector> punishedEffectors = new ArrayList<>();
    private final List<Neuron> neurons = new ArrayList<>();
    private final List<Node<?, ?>> deadendNodes = new ArrayList<>();
    private final List<Node<?, ?>> sidewayNodes = new ArrayList<>();
    private final List<Node<?, ?>> leafNodes = new ArrayList<>();
    private final List<NetworkEventsListener> listeners;
    private int timestamp = 0;
    Neuron targetNeuron = null;
    private final int maxNeuronSize;
    private final PainEffector painEffector;
    private boolean isPain = false;

    public Network(List<NetworkEventsListener> listeners, int maxNeuronSize) {
        this.listeners = listeners;
        this.maxNeuronSize = maxNeuronSize;
        painEffector = addPainEffector();
    }

    public Network(int maxNeuronSize) {
        this.listeners = new ArrayList<>();
        this.maxNeuronSize = maxNeuronSize;
        painEffector = addPainEffector();
    }

    private PainEffector addPainEffector() {
        PainEffector painEffector = new PainEffector(this);
        listeners.forEach(l -> l.onPainEffectorAdded(painEffector));
        return painEffector;
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
//        assert synapses.isEmpty() || synapses.stream().anyMatch(s -> s.getType() == Synapse.Type.EXCITATORY);
        if (synapses.stream().anyMatch(s -> s.getBackward() == Flow.RUN && s.getType() == Synapse.Type.EXCITATORY)) {
            return Flow.RUN;
        } else {
            return Flow.STILL;
        }
    }

    public Stream<Effector> streamOfEffectors() {
        return effectors.stream();
    }

    public Stream<Node<?, ?>> streamOfDeadendNodes() {
        return deadendNodes.stream();
    }

    public void resetState() {
        neurons.forEach(Neuron::resetState);
        receptors.forEach(Receptor::resetState);
    }

    /**
     * Adds a reception for single object.
     *
     * @param t       object supplier
     * @param adapter transformation function from source supplier to supplier of booleans
     * @param <T>     type parameter bounded to Supplier
     * @return just created {@link Receptor} instance
     */
    public <T extends Supplier<?>> Receptor addReceptor(T t, Function<T, BooleanSupplier> adapter) {
        return addReceptor(adapter.apply(t));
    }

    /**
     * Adds a set of receptors for objects being supplied by supplier.
     *
     * @param tSupplier      object supplier
     * @param u              compound parameter to use in mapping function
     * @param receptorCount  buckets count
     * @param receptorMapper mapping function from object being provided by supplier and receptor index
     * @param <T>            type parameter
     * @param <U>            type parameter
     */
    public <T, U> void addReceptorField(Supplier<T> tSupplier, U u, int receptorCount, BiFunction<T, U, Integer> receptorMapper) {
        for (int i = 0; i < receptorCount; i++) {
            int bdx = i;
            addReceptor(tSupplier, t1 -> () -> receptorMapper.apply(tSupplier.get(), u) == bdx);
        }
    }

    /**
     * Creates a receptors for each object in dictionary.
     * Dict is final and non-extensible.
     * If the supplier supplies unknown object nothing will happen, no one receptor will be excited by the object.
     *
     * @param valueSupplier a channel by which objects are delivered to set of receptors
     * @param dictionary    a non-empty set with exact object values to be mapped to initial receptors
     */
    public void addStrictDictReceptor(Supplier<Object> valueSupplier, Set<Object> dictionary) {
        AtomicInteger idx = new AtomicInteger();
        Map<Object, Integer> indexMapping = dictionary.stream().collect(Collectors.toMap(t -> t, t -> idx.incrementAndGet()));
        indexMapping.keySet().forEach(t -> {
            int index = indexMapping.getOrDefault(t, OUT_OF_RANGE_VALUE);
            addReceptor(valueSupplier, supplier -> () -> {
                Integer receptorIndex = indexMapping.get(supplier.get());
                return receptorIndex != null && receptorIndex.equals(index);
            });
        });
    }

    /**
     * Creates a receptors for each object in dictionary.
     * At runtime for each unknown object being supplied by supplier separate receptor will be created.
     * It is possible to call for method several times. Thus, several sets will be created, exactly one for every supplier.
     *
     * @param valueSupplier a channel by which objects are delivered to set of receptors
     * @param dictionary    a non-empty set with initial object values to be mapped to initial receptors
     */
    public void addAdaptiveDictReceptor(Supplier<Object> valueSupplier, Set<Object> dictionary) {
        if (dictionary.isEmpty()) {
            throw new RuntimeException("The dictionary cannot be empty");
        }
        AtomicInteger idx = new AtomicInteger();
        Map<Object, Integer> objectRegistry = dictionary.stream().collect(Collectors.toMap(t -> t, t -> idx.getAndIncrement()));
        objectRegistry.keySet().forEach(t -> addReceptor(valueSupplier, supplier -> () -> adaptiveEvaluator(valueSupplier, idx, objectRegistry, objectRegistry.get(t))));
    }

    private boolean adaptiveEvaluator(Supplier<Object> valueSupplier, AtomicInteger idx, Map<Object, Integer> objectRegistry, int testReceptorIndex) {
        Object value = valueSupplier.get();
        int receptorIndex = objectRegistry.getOrDefault(value, OUT_OF_RANGE_VALUE);
        if (receptorIndex == OUT_OF_RANGE_VALUE) {
            int rIndex = objectRegistry.computeIfAbsent(value, v -> idx.getAndIncrement());
            Receptor receptor = addReceptor(valueSupplier, supplier -> () -> adaptiveEvaluator(supplier, idx, objectRegistry, rIndex));
            receptor.triggerConverge();
            return false;
        } else {
            return testReceptorIndex == receptorIndex;
        }
    }

    public void addCharacterReception(Supplier<Character> characterSupplier, Character fromCharacter, int receptorCount) {
        addReceptorField(characterSupplier, fromCharacter, receptorCount, (character, character2) -> character - character2);
    }

    public void addDoubleReception(DoubleSupplier doubleSupplier, Bounds bounds, int receptorCount) {
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

    public Receptor addReceptor(BooleanSupplier booleanSupplier) {
        Receptor receptor = new Receptor(this, Objects.requireNonNull(booleanSupplier));
        addedReceptors.add(receptor);
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

    public void addPainReceptor(BooleanSupplier booleanSupplier) {
        Receptor receptor = addReceptor(booleanSupplier);
        connect(receptor, painEffector, Synapse.Type.EXCITATORY);
    }

    private Neuron addNeuron() {
        Neuron neuron = new Neuron(this);
        neurons.add(neuron);
        listeners.forEach(l -> l.onNeuronAdded(neuron));
        return neuron;
    }

    public void tick() {
        isPain = false;
        mergeReceptors();
        forwardPass();
        notifyListeners();
//        painEffector.streamOfOutputs().forEach(a -> a.setPunished(true));
        if (isPain){
            streamOfEffectors().forEach(e -> e.streamOfOutputs().forEach(a -> a.setPunished(e.getForwardFlow().toBoolean())));
        }
        backwardPass();
        listeners.forEach(l -> l.onDeadendNodesDetected(deadendNodes, receptors.size() + neurons.size()));
        createNewConnections();
        streamOfEffectors().forEach(e -> e.streamOfOutputs().forEach(a -> a.setPunished(false)));
        increaseTimestamp();
    }

    private void mergeReceptors() {
        if (!addedReceptors.isEmpty()) {
            receptors.addAll(addedReceptors);
            addedReceptors.clear();
        }
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
            l.onPainEffectorStateChanged(painEffector);
        });
    }

    private void backwardPass() {
        effectors.forEach(Node::triggerBackpass);
        leafNodes.forEach(Node::triggerBackpass);
        painEffector.triggerBackpass();
        leafNodes.clear();
    }

    private void forwardPass() {
        receptors.forEach(Node::triggerConverge);
    }

    private void createNewConnections() {
        if (!deadendNodes.isEmpty() && neurons.size() < maxNeuronSize) {
            targetNeuron = addNeuron();
            deadendNodes.forEach(d -> connect(d, targetNeuron, Synapse.Type.EXCITATORY, Flow.RUN, Flow.STILL));
            sidewayNodes.forEach(d -> connect(d, targetNeuron, Synapse.Type.INHIBITORY));
            runEffectors.forEach(d -> connect(targetNeuron, d, Synapse.Type.EXCITATORY, Flow.STILL, Flow.STILL));
            punishedEffectors.forEach(d -> connect(targetNeuron, d, Synapse.Type.INHIBITORY));
        }
        deadendNodes.clear();
        sidewayNodes.clear();
        runEffectors.clear();
        punishedEffectors.clear();
    }

    private void connect(Node source, Node target, Synapse.Type type, Flow forwardFlow, Flow backwardFlow) {
        assert !source.equals(target);
        Synapse synapse = new Synapse<>(source, target, type, forwardFlow, backwardFlow);
        source.addOutput(synapse);
        target.addInput(synapse);
        listeners.forEach(l -> l.onSynapseAdded(synapse));
    }

    private void connect(Node source, Node target, Synapse.Type type) {
        assert !source.equals(target);
        Synapse synapse = new Synapse<>(source, target, type);
        source.addOutput(synapse);
        target.addInput(synapse);
        listeners.forEach(l -> l.onSynapseAdded(synapse));
    }

    public void onRunEffectorFound(Effector effector) {
        runEffectors.add(effector);
    }

    public void onPunishedEffectorFound(Effector effector) {
        punishedEffectors.add(effector);
    }

    @Override
    public void onDeadendNodeFound(Node<?, ?> node) {
        assert !deadendNodes.contains(node) : timestamp + ": " + node.toString();
        assert !(node instanceof Effector) : timestamp + ": " + node.toString();
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
        assert !(node instanceof Effector) : timestamp + ": " + node.toString();
        sidewayNodes.add(node);
    }

    @Override
    public void onLeafNodeFound(Node<?, ?> node) {
        leafNodes.add(node);
    }

    public int getNeuronCount() {
        return neurons.size();
    }

    public int getReceptorCount() {
        return receptors.size();
    }

    public int getEffectorCount() {
        return effectors.size();
    }

    public int getNodesCount() {
        int cnt = Optional.ofNullable(painEffector).isPresent() ? 1 : 0;
        return neurons.size() + receptors.size() + addedReceptors.size() + effectors.size() + cnt;
    }

    public void applyPainMode() {
        isPain = true;
    }
}
