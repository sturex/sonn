package playground;

import core.Node;
import neural.Bounds;
import neural.Network;
import neural.NetworkEventsListener;
import util.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OutlierDetectionPlayground {
    private static final int lowerBound = -100;
    private static final int upperBound = 100;
    public static final Bounds bounds = new Bounds(lowerBound, upperBound);
    private static final int bCount = 100;
    private static final int maxNeuronSize = 2000;
    public static final String delimiter = ";";
    private static int tdidx = 0;

    public static void main(String[] args) throws IOException, InterruptedException {

        StringBuilder sb = new StringBuilder();
        double[][] trafficData = Util.readAsDoubleArray(Path.of("src/main/resources/data/traffic.csv"), delimiter);

        List<NetworkEventsListener> listeners = List.of(new NetworkEventsListener() {
            @Override
            public void onDeadendNodesDetected(List<Node<?, ?>> deadendNodes, int maxDeadendNeuronCount) {
                List<Node<?, ?>> nodes = deadendNodes.stream().sorted(Comparator.comparingInt(Node::getId)).collect(Collectors.toList());
                double average = deadendNodes.stream().mapToInt(Node::getId).summaryStatistics().getAverage();
                sb
                        .append(1. - (double) nodes.get(nodes.size() - 1).getId() / (double) maxDeadendNeuronCount).append(delimiter)
                        .append(1. - average / (double) maxDeadendNeuronCount).append(delimiter)
                        .append(maxDeadendNeuronCount).append(delimiter)
                        .append(deadendNodes.stream().map(n -> String.valueOf(n.getId())).sorted().collect(Collectors.joining(delimiter)));
            }
        });

        Network network = new Network(listeners, maxNeuronSize);

        network.addDoubleReception(() -> trafficData[tdidx][0], bounds, bCount);
        network.addDoubleReception(() -> trafficData[tdidx][1], bounds, bCount);
        network.addDoubleReception(() -> trafficData[tdidx][2], bounds, bCount);

        while (tdidx < trafficData.length - 1 && tdidx < 5000) {
            tdidx++;
            sb.append(tdidx).append(delimiter);
            sb.append(Arrays.stream(trafficData[tdidx]).mapToObj(String::valueOf).collect(Collectors.joining(delimiter))).append(delimiter);
            network.tick();
            sb.append("\n");
            System.out.println(tdidx);
//            Thread.sleep(300);
        }

        Files.write(Path.of("src/main/resources/logs/traffic_output.csv"), sb.toString().getBytes());
    }

}
