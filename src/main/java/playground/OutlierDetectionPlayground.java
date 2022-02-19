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
    private static final int lowerBound = -2;
    private static final int upperBound = 3;
    public static final Bounds bounds = new Bounds(lowerBound, upperBound);
    private static final int bCount = 100;
    private static final int maxNeuronSize = 500;
    public static final String delimiter = ";";
    private static int colidx = 0;

    public static void main(String[] args) throws IOException, InterruptedException {

        StringBuilder sb = new StringBuilder();
        double[][] ecgData = Util.readAsDoubleArray(Path.of("src/main/resources/data/ecg_edit.csv"), delimiter);

        List<NetworkEventsListener> listeners = List.of(new NetworkEventsListener() {
            @Override
            public void onDeadendNodesDetected(List<Node<?, ?>> deadendNodes, int maxDeadendNeuronCount) {
                List<Node<?, ?>> nodes = deadendNodes.stream().sorted(Comparator.comparingInt(Node::getId)).collect(Collectors.toList());
                double average = deadendNodes.stream().mapToInt(Node::getId).summaryStatistics().getAverage();
                sb
                        .append(average).append(delimiter)
                        .append(1. - (double) nodes.get(nodes.size() - 1).getId() / (double) maxDeadendNeuronCount).append(delimiter)
                        .append(1. - average / (double) maxDeadendNeuronCount).append(delimiter)
//                        .append(maxDeadendNeuronCount).append(delimiter)
                        .append(deadendNodes.stream().map(n -> String.valueOf(n.getId())).sorted().collect(Collectors.joining(delimiter)));
            }
        });

        Network network = new Network(listeners, maxNeuronSize);

        network.addDoubleReception(() -> ecgData[colidx][0], bounds, bCount);
//        network.addDoubleReception(() -> ecgData[colidx][1], bounds, bCount);

        while (colidx < ecgData.length - 1 && colidx < 10000) {
            colidx++;
            sb.append(colidx).append(delimiter);
            sb.append(Arrays.stream(ecgData[colidx]).mapToObj(String::valueOf).collect(Collectors.joining(delimiter))).append(delimiter);
            network.tick();
            sb.append("\n");
            System.out.println(colidx);
//            Thread.sleep(300);
        }

        Files.write(Path.of("src/main/resources/logs/ecg_output.csv"), sb.toString().getBytes());
    }

}
