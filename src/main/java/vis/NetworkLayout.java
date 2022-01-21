package vis;

public interface NetworkLayout {

    void addInputNode(LayoutInputNode inputNode);

    void addOutputNode(LayoutOutputNode outputNode);

    void addInnerNode(LayoutInnerNode innerNode);

    void addEdge(LayoutEdge layoutEdge);

    void updateEdge(LayoutEdge layoutEdge);

    void updateInputNode(LayoutInputNode inputNode, boolean isEnlarged);

    void updateOutputNode(LayoutOutputNode outputNode, boolean isEnlarged);
}
