package vis;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public class GraphStreamStaticLayout implements NetworkLayout {

    public static final String INPUT = "input";
    public static final String OUTPUT = "output";
    public static final String INNER = "inner";
    private final SingleGraph graph;
    private static final int X_START = 0;
    private static final int Y_START = 0;
    private int horizontalCount = 0;
    private int verticalCount = 0;

    private record Coords(int x, int y) {
    }

    public GraphStreamStaticLayout() {
        System.setProperty("org.graphstream.ui", "swing");
        graph = new SingleGraph("0");
        graph.setAttribute("ui.stylesheet", readStyles("layout/staticLayoutStyles.css"));
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        Viewer viewer = graph.display(false);
    }

    private String readStyles(String fileName) {
        URL resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            try {
                return Files.readString(new File(resource.toURI()).toPath());
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException("Couldn't read styles from resources");
            }
        }
    }

    @Override
    public void addInputNode(LayoutInputNode inputNode) {
        org.graphstream.graph.Node node = addNode(new Coords(++horizontalCount, Y_START),
                getHorizontalNodeId(inputNode.getId()), INPUT);
        node.setAttribute("ui.label", node.getId());
        updateHorizontalNode(inputNode);
    }

    private void updateHorizontalNode(LayoutInputNode inputNode) {
        org.graphstream.graph.Node graphNode = graph.getNode(getHorizontalNodeId(inputNode.getId()));
        if (inputNode.getTotalOutputs() == 0) {
            graphNode.setAttribute("ui.pie-values", 0, 0, 1);
        } else {
            graphNode.setAttribute("ui.pie-values",
                    (double) inputNode.getPositiveOutputs() / inputNode.getTotalOutputs(),
                    (double) inputNode.getNegativeOutputs() / inputNode.getTotalOutputs(), 0.0);
        }
    }

    private org.graphstream.graph.Node addNode(Coords coords, String nodeId, String nodeType) {
        org.graphstream.graph.Node node = graph.addNode(nodeId);
        node.setAttribute("xyz", coords.x, coords.y, 0);
        node.setAttribute("ui.class", nodeType);
        return node;
    }

    @Override
    public void addOutputNode(LayoutOutputNode outputNode) {
        org.graphstream.graph.Node node = addNode(new Coords(X_START, ++verticalCount),
                getVerticalNodeId(outputNode.getId()), OUTPUT);
        node.setAttribute("ui.label", node.getId());
        updateVerticalNode(outputNode);
    }

    private void updateVerticalNode(LayoutOutputNode outputNode) {
        org.graphstream.graph.Node graphNode = graph.getNode(getVerticalNodeId(outputNode.getId()));
        if (outputNode.getTotalInputs() == 0) {
            graphNode.setAttribute("ui.pie-values", 0, 0, 1);
        } else {
            graphNode.setAttribute("ui.pie-values",
                    (double) outputNode.getPositiveInputs() / outputNode.getTotalInputs(),
                    (double) outputNode.getNegativeInputs() / outputNode.getTotalInputs(), 0.0);
        }
    }

    @Override
    public void addInnerNode(LayoutInnerNode innerNode) {
        org.graphstream.graph.Node verticalNode = graph.getNode(getVerticalNodeId(innerNode.getTargetId()));
        org.graphstream.graph.Node horizontalNode = graph.getNode(getHorizontalNodeId(innerNode.getSourceId()));

        int[] xyzV = verticalNode.getAttribute("xyz", int[].class);
        int[] xyzH = horizontalNode.getAttribute("xyz", int[].class);

        org.graphstream.graph.Node node = addNode(new Coords(xyzH[0], xyzV[1]),
                getInnerNodeId(innerNode.getSourceId(), innerNode.getTargetId()), INNER);
        node.setAttribute("ui.label", node.getId());
    }

    @Override
    public void updateNode() {

    }

    @Override
    public void updateInnerNode() {

    }

    private String getVerticalNodeId(int id) {
        return "V_" + id;
    }

    private String getHorizontalNodeId(int id) {
        return "H_" + id;
    }

    private String getInnerNodeId(int sourceId, int targetId) {
        return "I_" + sourceId + "_" + targetId;
    }
}
