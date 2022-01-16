package core;

import java.util.Objects;

public abstract class Edge<T extends Node<? extends FlowSupplier, ? extends FlowConsumer>, U extends Node<? extends FlowSupplier, ? extends FlowConsumer>> implements FlowSupplier, FlowConsumer {

    private final T input;
    private final U output;
    private Flow forwardFlow = Flow.STILL;
    private Flow backwardFlow = Flow.STILL;

    public Edge(T input, U output) {
        this.input = input;
        this.output = output;
    }

    public void setForwardFlow(Flow forwardFlow) {
        this.forwardFlow = Objects.requireNonNull(forwardFlow);
    }

    public void setBackwardFlow(Flow backwardFlow) {
        this.backwardFlow = Objects.requireNonNull(backwardFlow);
    }

    public T getInput() {
        return input;
    }

    public U getOutput() {
        return output;
    }

    @Override
    public final void acceptForward(Flow flow) {
        forwardFlow = combineForwardFlow(forwardFlow, backwardFlow, flow);
        output.collectInput();
    }

    @Override
    public final void acceptBackward(Flow flow) {
        backwardFlow = combineBackwardFlow(forwardFlow, backwardFlow, flow);
        input.collectOutput();
    }

    protected Flow combineForwardFlow(Flow forwardFlow, Flow backwardFlow, Flow acceptedFlow) {
        return acceptedFlow;
    }

    protected Flow combineBackwardFlow(Flow forwardFlow, Flow backwardFlow, Flow acceptedFlow) {
        return acceptedFlow;
    }

    @Override
    public Flow getForward() {
        return forwardFlow;
    }

    @Override
    public Flow getBackward() {
        return backwardFlow;
    }

    public boolean isForwardRun() {
        return forwardFlow == Flow.RUN;
    }
}