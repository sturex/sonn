package vis;

import core.Node;

public interface LayoutInnerNode {

    int getSourceId();

    int getTargetId();

    static LayoutInnerNode of(Node<?, ?> input, Node<?, ?> output) {
        return new LayoutInnerNode() {
            @Override
            public int getSourceId() {
                return input.getId();
            }

            @Override
            public int getTargetId() {
                return output.getId();
            }
        };
    }
}
