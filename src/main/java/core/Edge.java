package core;

public abstract class Edge<T extends Node<? extends FlowSupplier, ? extends FlowConsumer>, U extends Node<? extends FlowSupplier, ? extends FlowConsumer>> implements FlowSupplier, FlowConsumer {

    private final T input;
    private final U output;
    private Flow forwardFlow = Flow.STILL;
    private Flow backwardFlow = Flow.STILL;
    private static int idCounter = 0;
    private final int id = idCounter++;

    public Edge(T input, U output) {
        this.input = input;
        this.output = output;
    }

    public T getInput() {
        return input;
    }

    public U getOutput() {
        return output;
    }

    public int getId() {
        return id;
    }

    @Override
    public void acceptForward(Flow flow) {
        forwardFlow = flow;
        output.collectInput();
    }

    @Override
    public void acceptBackward(Flow flow) {
        backwardFlow = flow;
        input.collectOutput();
    }

    @Override
    public Flow getForward() {
        return backwardFlow;
    }

    @Override
    public Flow getBackward() {
        return forwardFlow;
    }
}