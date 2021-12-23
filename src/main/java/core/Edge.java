package core;

public abstract class Edge<T extends Node<? extends FlowSupplier, ? extends FlowConsumer>, U extends Node<? extends FlowSupplier, ? extends FlowConsumer>> implements FlowSupplier, FlowConsumer {

    private final T input;
    private final U output;
    private Flow flow;

    public Edge(T input, U output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void accept(Flow flow) {
        this.flow = flow;
        output.collectInput();
    }

    @Override
    public Flow get() {
        return flow;
    }

}