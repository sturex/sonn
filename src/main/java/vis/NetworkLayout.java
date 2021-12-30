package vis;

public interface NetworkLayout {

    void addInputNode(LayoutInputNode inputNode);

    void addOutputNode(LayoutOutputNode outputNode);

    void addInnerNode(LayoutInnerNode innerNode, boolean isGreen);

    void updateInnerNode(LayoutInnerNode innerNode, boolean isRun, boolean isGreen);

    void updateInputNode(LayoutInputNode inputNode, boolean isEnlarged);

    void updateOutputNode(LayoutOutputNode outputNode, boolean isEnlarged);
}
