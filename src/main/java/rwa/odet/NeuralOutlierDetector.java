package rwa.odet;

import core.Node;
import neural.Bounds;
import neural.Network;
import neural.NetworkEventsListener;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class NeuralOutlierDetector implements OutlierDetector {

    private final Network network;
    private double outlierEstimation = 0;

    public NeuralOutlierDetector(int maxNeuronSize) {
        network = new Network(Collections.singletonList(new NetworkEventsListener() {
            @Override
            public void onDeadendNodesDetected(List<Node<?, ?>> deadendNodes, int maxDeadendNeuronCount) {
                double average = deadendNodes.stream().mapToInt(Node::getId).summaryStatistics().getAverage();
                outlierEstimation = 1. - average / (double) maxDeadendNeuronCount;
            }
        }), maxNeuronSize);
    }

    @Override
    public void addChannel(DoubleSupplier ds, Bounds bounds, int bucketCount) {
        network.addDoubleReception(ds, bounds, bucketCount);
    }

    @Override
    public void addAdaptiveChannel(Supplier<Object> supplier, Set<Object> dictionary) {
        network.addAdaptiveDictReceptor(supplier, dictionary);
    }

    @Override
    public double next() {
        network.tick();
        return outlierEstimation;
    }
}
