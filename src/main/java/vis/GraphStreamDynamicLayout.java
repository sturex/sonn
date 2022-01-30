package vis;

import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import util.Util;

public class GraphStreamDynamicLayout implements NetworkLayout {

    private final SingleGraph graph;

    public GraphStreamDynamicLayout() {
        System.setProperty("org.graphstream.ui", "swing");
        graph = new SingleGraph("0");
        graph.setAttribute("ui.stylesheet", Util.readAsString("layout/dynamicLayoutStyles.css"));
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        Viewer viewer = graph.display(true);
    }

    @Override
    public void addInputNode(LayoutInputNode inputNode) {
        org.graphstream.graph.Node node = graph.addNode(String.valueOf(inputNode.getId()));
        node.setAttribute("ui.class", inputNode.getUiClass());
    }

    @Override
    public void addOutputNode(LayoutOutputNode outputNode) {
        org.graphstream.graph.Node node = graph.addNode(String.valueOf(outputNode.getId()));
        node.setAttribute("ui.class", outputNode.getUiClass());
    }

    @Override
    public void addInnerNode(LayoutInnerNode innerNode) {
        org.graphstream.graph.Node node = graph.addNode(String.valueOf(innerNode.getId()));
        node.setAttribute("ui.class", innerNode.getUiClass());
    }

    @Override
    public void addEdge(LayoutEdge layoutEdge) {
        org.graphstream.graph.Edge edge = graph.addEdge(getEdgeId(layoutEdge.getSourceId(), layoutEdge.getTargetId()),
                String.valueOf(layoutEdge.getSourceId()),
                String.valueOf(layoutEdge.getTargetId()), true);
        edge.setAttribute("ui.class", layoutEdge.getUiClass());
    }

    @Override
    public void updateEdge(LayoutEdge layoutEdge) {
        Edge edge = graph.getEdge(getEdgeId(layoutEdge.getSourceId(), layoutEdge.getTargetId()));
        edge.setAttribute("ui.class", layoutEdge.getUiClass());
    }

    @Override
    public void updateInputNode(LayoutInputNode inputNode, boolean isEnlarged) {
        org.graphstream.graph.Node graphNode = graph.getNode(String.valueOf(inputNode.getId()));
        String prefix = isEnlarged ? "bypassed" : "still";
        graphNode.setAttribute("ui.class", prefix + "_" + inputNode.getUiClass());
    }

    @Override
    public void updateOutputNode(LayoutOutputNode outputNode, boolean isEnlarged) {
        org.graphstream.graph.Node graphNode = graph.getNode(String.valueOf(outputNode.getId()));
        String prefix = isEnlarged ? "bypassed" : "still";
        graphNode.setAttribute("ui.class", prefix + "_" + outputNode.getUiClass());
        graphNode.setAttribute("ui.size", isEnlarged ? "1.2gu" : "0.8gu");
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private String getEdgeId(int sourceId, int targetId) {
        return "I_" + sourceId + "_" + targetId;
    }
}
