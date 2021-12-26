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
    private final NodeEventListener nodeEventListener;

    protected Node(NodeEventListener nodeEventListener) {
        this.nodeEventListener = nodeEventListener;
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

    public final void triggerConverge() {
        forwardFlow = convergeForward(inputs.stream());
        outputs.forEach(output -> output.acceptForward(forwardFlow));
    }

    public void triggerBackpass() {
        backwardFlow = convergeBackward(outputs.stream());
        Flow combinedFlow = nodeEventListener.onFlowsCrossed(this, forwardFlow, backwardFlow);
        inputs.forEach(input -> input.acceptBackward(combinedFlow));
    }

    public final int inputSize() {
        return inputs.size();
    }

    public final int outputSize() {
        return outputs.size();
    }

    private boolean everyInputCollected() {
        if (collectedInputCounter++ == inputs.size()) {
            collectedInputCounter = 0;
            return true;
        }
        return false;
    }

    private boolean everyOutputCollected() {
        if (collectedOutputCounter++ == outputs.size()) {
            collectedOutputCounter = 0;
            return true;
        }
        return false;
    }

    public abstract Flow convergeForward(Stream<T> stream);

    public abstract Flow convergeBackward(Stream<U> stream);

    public final void addInput(T t) {
        assert !inputs.contains(t);
        inputs.add(t);
    }

    public final void addOutput(U u) {
        assert !outputs.contains(u);
        outputs.add(u);
    }
}
