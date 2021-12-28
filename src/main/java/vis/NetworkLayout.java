package vis;

public interface NetworkLayout {

    void addInputNode(LayoutInputNode inputNode);

    void addOutputNode(LayoutOutputNode outputNode);

    void addInnerNode(LayoutInnerNode innerNode, boolean isGreen);

    void updateNode(int id, boolean isEnlarged);

    void updateInnerNode(LayoutInnerNode innerNode, boolean isRun, boolean isGreen);

}
