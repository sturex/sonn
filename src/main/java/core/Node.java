package core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class Node<T extends FlowSupplier, U extends FlowConsumer> {

    private final List<T> inputs = new ArrayList<>();
    private final List<U> outputs = new ArrayList<>();
    private long collectedInputCounter;
    private long collectedOutputCounter;
    private Flow forwardFlow = Flow.STILL;
    private Flow backwardFlow = Flow.STILL;
    private static int idCounter = 0;
    private final int id = idCounter++;

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
        if (everyInputCollected()) {
            triggerConverge();
        }
    }

    final void collectOutput() {
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

    public boolean isStill() {
        return forwardFlow == backwardFlow && forwardFlow == Flow.STILL;
    }

    public boolean isRun() {
        return forwardFlow == backwardFlow && forwardFlow == Flow.RUN;
    }

    public boolean isDeadend() {
        return (forwardFlow == Flow.RUN && backwardFlow == Flow.STILL) || outputSize() == 0;
    }

    public boolean isSideway() {
        return forwardFlow == Flow.STILL && backwardFlow == Flow.RUN;
    }

    public final void triggerConverge() {
        forwardFlow = convergeForward(inputs);
        outputs.forEach(output -> output.acceptForward(forwardFlow));
    }

    public void triggerBackpass() {
        backwardFlow = convergeBackward(outputs);
        inputs.forEach(input -> input.acceptBackward(backwardFlow));
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node node) {
            return id == node.getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return id + ": " + forwardFlow.toString() + "-" + backwardFlow.toString();
    }
}
