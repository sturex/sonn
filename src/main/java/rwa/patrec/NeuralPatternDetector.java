package rwa.patrec;

import core.Node;
import neural.Network;
import neural.NetworkEventsListener;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class NeuralPatternDetector implements PatternDetector {

    private final Network network;
    private int[] patternIds;

    public NeuralPatternDetector(int maxNeuronSize) {
        network = new Network(maxNeuronSize);
        network.addListener(new NetworkEventsListener() {
            @Override
            public void onDeadendNodesDetected(List<Node<?, ?>> deadendNodes, int maxDeadendNeuronCount) {
                patternIds = deadendNodes.stream().mapToInt(Node::getId).sorted().toArray();
            }
        });
    }

    @Override
    public void addStrictDictChannel(Supplier<Object> supplier, Set<Object> dictionary) {
        network.addStrictDictReceptor(supplier, dictionary);
    }

    @Override
    public void addAdaptiveDictChannel(Supplier<Object> supplier, Set<Object> dictionary) {
        network.addAdaptiveDictReceptor(supplier, dictionary);
    }

    @Override
    public int[] next() {
        network.tick();
        return patternIds;
    }

    @Override
    public void reset() {
        network.resetState();
    }
}
