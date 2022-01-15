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
import java.util.List;
import java.util.stream.Collectors;

public class OutlierDetectionPlayground {
    private static final int lowerBound = 0;
    private static final int upperBound = 100;
    public static final Bounds bounds = new Bounds(lowerBound, upperBound);
    private static final int bCount = 10;
    private static final int maxNeuronSize = 10000;
    public static final String delimiter = ";";
    private static int tdidx = 0;

    public static void main(String[] args) throws IOException {

        StringBuilder sb = new StringBuilder();
        double[][] trafficData = Util.readAsDoubleArray(Path.of("src/main/resources/data/traffic.csv"), delimiter);

        Network network = new Network(maxNeuronSize);

        network.addListener(new NetworkEventsListener() {
            @Override
            public void onDeadendNodesDetected(List<Node<?, ?>> deadendNodes, int maxDeadendNeuronCount) {
                sb.append(deadendNodes.stream().map(n -> maxDeadendNeuronCount + ";" + String.valueOf(n.getId())).collect(Collectors.joining(delimiter)));
            }
        });

        network.addDoubleReception(() -> trafficData[tdidx][0], bounds, bCount);
        network.addDoubleReception(() -> trafficData[tdidx][1], bounds, bCount);
        network.addDoubleReception(() -> trafficData[tdidx][2], bounds, bCount);

        while (tdidx < 5000) {
            tdidx++;
            sb.append(tdidx).append(delimiter);
            sb.append(Arrays.stream(trafficData[tdidx]).mapToObj(String::valueOf).collect(Collectors.joining(delimiter))).append(delimiter);
            network.tick();
            sb.append("\n");
            System.out.println(tdidx);
        }

        Files.write(Path.of("src/main/resources/logs/traffic_output.csv"), sb.toString().getBytes());
    }

}
