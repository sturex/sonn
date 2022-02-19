package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class Node<T extends FlowSupplier, U extends FlowConsumer> {

    private final Graph graph;
    private final List<T> inputs = new ArrayList<>();
    private final List<U> outputs = new ArrayList<>();
    private long collectedInputCounter;
    private long collectedOutputCounter;
    private Flow forwardFlow = Flow.STILL;
    private Flow backwardFlow = Flow.STILL;
    private final int id;
    //TODO very ugly solution
    private boolean isParent = false;

    public void setParent() {
        isParent = true;
    }

    protected Node(Graph graph, int id) {
        this.graph = graph;
        this.id = id;
    }

    public void setForwardFlow(Flow forwardFlow) {
        this.forwardFlow = Objects.requireNonNull(forwardFlow);
    }

    public void setBackwardFlow(Flow backwardFlow) {
        this.backwardFlow = Objects.requireNonNull(backwardFlow);
    }

    public int getId() {
        return id;
    }

    public Flow getForwardFlow() {
        return forwardFlow;
    }

    public Flow getBackwardFlow() {
        return backwardFlow;
    }

    final void collectInput() {
        assert collectedInputCounter < inputSize() : toString();
        if (everyInputCollected()) {
            triggerConverge();
        }
    }

    final void collectOutput() {
        assert collectedOutputCounter < outputSize() : toString();
        if (everyOutputCollected()) {
            triggerBackpass();
        }
    }

    public Stream<T> streamOfInputs() {
        return inputs.stream();
    }

    public Stream<U> streamOfOutputs() {
        return outputs.stream();
    }

    public boolean isForwardRun() {
        return forwardFlow == Flow.RUN;
    }

    public boolean isBackwardRun() {
        return backwardFlow == Flow.RUN;
    }

    private boolean isDeadend() {
        return forwardFlow == Flow.RUN && backwardFlow == Flow.STILL;
    }

    private boolean isSideway() {
        return forwardFlow == Flow.STILL && backwardFlow == Flow.RUN && isParent;
    }

    public final void triggerConverge() {
        assert collectedInputCounter == 0;
        isParent = false;
        assert inputSize() != 0 : toString();
        forwardFlow = convergeForward(inputs);
        if (outputSize() == 0) {
            onLeafFound();
            if (isForwardRun()) {
                onDeadendFound();
            }
        } else {
            outputs.forEach(output -> output.acceptForward(forwardFlow));
        }
    }

    private void onLeafFound() {
        graph.onLeafNodeFound(this);
    }

    public void triggerBackpass() {
        assert collectedOutputCounter == 0;
        if (outputSize() != 0) {
            backwardFlow = convergeBackward(outputs);
            if (isDeadend()) {
                onDeadendFound();
            } else if (isSideway()) {
                onSidewayFound();
            } else if (isForwardRun() && isBackwardRun()) {
                onRunFound();
            }
        }
//        assert inputSize() != 0 : toString();
        inputs.forEach(input -> input.acceptBackward(forwardFlow));
    }

    protected void onRunFound() {

    }

    protected void onDeadendFound() {
        graph.onDeadendNodeFound(this);
    }

    private void onSidewayFound() {
        graph.onSidewayNodeFound(this);
    }

    public final int inputSize() {
        return inputs.size();
    }

    public final int outputSize() {
        return outputs.size();
    }

    private boolean everyInputCollected() {
        if (++collectedInputCounter == inputs.size()) {
            collectedInputCounter = 0;
            return true;
        }
        return false;
    }

    private boolean everyOutputCollected() {
        if (++collectedOutputCounter == outputs.size()) {
            collectedOutputCounter = 0;
            return true;
        }
        return false;
    }

    public abstract Flow convergeForward(List<T> ts);

    public abstract Flow convergeBackward(List<U> us);

    public final void addInput(T t) {
        assert !inputs.contains(t);
        inputs.add(t);
    }

    public final void addOutput(U u) {
        assert !outputs.contains(u);
        outputs.add(u);
    }

    public void resetFlows() {
        backwardFlow = Flow.STILL;
        forwardFlow = Flow.STILL;
    }

    @Override
    public String toString() {
        return id + " (" + forwardFlow + "-" + backwardFlow + ")";
    }
}
