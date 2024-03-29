package vis;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import util.Util;

import java.util.Optional;

public class GraphStreamStaticLayout implements NetworkLayout {

    public static final String INITIAL_POSITIVE = "initial_positive";
    public static final String INITIAL_NEGATIVE = "initial_negative";
    public static final String BYPASSED_POSITIVE = "bypassed_positive";
    public static final String BYPASSED_NEGATIVE = "bypassed_negative";
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
        graph.setAttribute("ui.stylesheet", Util.readAsString("layout/staticLayoutStyles.css"));
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        Viewer viewer = graph.display(false);
    }

    @Override
    public void addInputNode(LayoutInputNode inputNode) {
        org.graphstream.graph.Node node = addNode(new Coords(++horizontalCount, Y_START),
                getHorizontalNodeId(inputNode.getId()), inputNode.getUiClass());
        node.setAttribute("ui.label", node.getId());
        updateHorizontalNode(inputNode, false);
    }

    private void updateHorizontalNode(LayoutInputNode inputNode, boolean isEnlarged) {
        org.graphstream.graph.Node graphNode = graph.getNode(getHorizontalNodeId(inputNode.getId()));
        if (inputNode.getTotalOutputs() == 0) {
            graphNode.setAttribute("ui.pie-values", 0, 0, 1);
        } else {
            graphNode.setAttribute("ui.pie-values",
                    (double) inputNode.getPositiveOutputs() / inputNode.getTotalOutputs(),
                    (double) inputNode.getNegativeOutputs() / inputNode.getTotalOutputs(), 0.0);
        }
        graphNode.setAttribute("ui.size", isEnlarged ? "1.2gu" : "0.8gu");
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
                getVerticalNodeId(outputNode.getId()), outputNode.getUiClass());
        node.setAttribute("ui.label", node.getId());
        updateVerticalNode(outputNode, false);
    }

    @Override
    public void addInnerNode(LayoutInnerNode innerNode) {
        addOutputNode(LayoutOutputNode.of(innerNode));
        addInputNode(LayoutInputNode.of(innerNode));
    }

    private void updateVerticalNode(LayoutOutputNode outputNode, boolean isEnlarged) {
        org.graphstream.graph.Node graphNode = graph.getNode(getVerticalNodeId(outputNode.getId()));
        if (outputNode.getTotalInputs() == 0) {
            graphNode.setAttribute("ui.pie-values", 0, 0, 1);
        } else {
            graphNode.setAttribute("ui.pie-values",
                    (double) outputNode.getPositiveInputs() / outputNode.getTotalInputs(),
                    (double) outputNode.getNegativeInputs() / outputNode.getTotalInputs(), 0.0);
        }
        graphNode.setAttribute("ui.size", isEnlarged ? "1.2gu" : "0.8gu");
    }

    @Override
    public void addEdge(LayoutEdge layoutEdge) {
        org.graphstream.graph.Node verticalNode = graph.getNode(getVerticalNodeId(layoutEdge.getTargetId()));
        org.graphstream.graph.Node horizontalNode = graph.getNode(getHorizontalNodeId(layoutEdge.getSourceId()));
        Object[] xyzV = verticalNode.getAttribute("xyz", Object[].class);
        Object[] xyzH = horizontalNode.getAttribute("xyz", Object[].class);
        org.graphstream.graph.Node node = addNode(new Coords((int) xyzH[0], (int) xyzV[1]),
                getInnerNodeId(layoutEdge.getSourceId(), layoutEdge.getTargetId()), layoutEdge.getUiClass());
        node.setAttribute("ui.label", node.getId());
        node.setAttribute("ui.class", layoutEdge.getUiClass());
    }

    @Override
    public void updateEdge(LayoutEdge layoutEdge) {
        String id = getInnerNodeId(layoutEdge.getSourceId(), layoutEdge.getTargetId());
        Optional.ofNullable(graph.getNode(id))
                .orElseThrow(() -> new RuntimeException("Not found inner node with id: " + id))
                .setAttribute("ui.class", layoutEdge.getUiClass());
    }

    @Override
    public void updateInputNode(LayoutInputNode inputNode, boolean isEnlarged) {
        updateHorizontalNode(inputNode, isEnlarged);
    }

    @Override
    public void updateOutputNode(LayoutOutputNode outputNode, boolean isEnlarged) {
        updateVerticalNode(outputNode, isEnlarged);
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
