package vis;

public interface NetworkLayout {

    void addInputNode(LayoutInputNode inputNode);

    void addOutputNode(LayoutOutputNode outputNode);

    void addInnerNode(LayoutInnerNode innerNode);

    void updateNode(int id, boolean isEnlarged);

    void updateInnerNode();

}
