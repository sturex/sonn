package vis;

public interface NetworkLayout {

    void addInputNode(LayoutInputNode inputNode);

    void addOutputNode(LayoutOutputNode outputNode);

    void addInnerNode(LayoutInnerNode innerNode);

    void addEdge(LayoutEdge layoutEdge);

    void updateInnerNode(LayoutEdge layoutEdge);

    void updateInputNode(LayoutInputNode inputNode, boolean isEnlarged);

    void updateOutputNode(LayoutOutputNode outputNode, boolean isEnlarged);
}
