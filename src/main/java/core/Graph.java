package core;

public interface Graph {
    void onDeadendNodeFound(Node<?, ?> node);

    void onSidewayNodeFound(Node<?, ?> node);
}
