package vis;

import core.Node;

public interface LayoutInnerNode {

    int getSourceId();

    int getTargetId();

    static LayoutInnerNode of(Node<?, ?> input, Node<?, ?> output) {
        return null;
    }
}
