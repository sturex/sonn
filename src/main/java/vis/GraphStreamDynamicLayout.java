package vis;

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
        org.graphstream.graph.Edge edge = graph.addEdge(getInnerNodeId(layoutEdge.getSourceId(), layoutEdge.getTargetId()),
                String.valueOf(layoutEdge.getSourceId()),
                String.valueOf(layoutEdge.getTargetId()), true);
        edge.setAttribute("ui.class", layoutEdge.getUiClass());
    }

    @Override
    public void updateInnerNode(LayoutEdge layoutEdge) {

    }

    @Override
    public void updateInputNode(LayoutInputNode inputNode, boolean isEnlarged) {
        //graph.getNode(String.valueOf(inputNode.getId())).setAttribute();
    }

    @Override
    public void updateOutputNode(LayoutOutputNode outputNode, boolean isEnlarged) {

    }

    private String getInnerNodeId(int sourceId, int targetId) {
        return "I_" + sourceId + "_" + targetId;
    }
}
