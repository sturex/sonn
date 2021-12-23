package core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class Node<T extends FlowSupplier, U extends FlowConsumer> {

    private final List<T> inputs = new ArrayList<>();
    private final List<U> outputs = new ArrayList<>();
    private long collectedInputCounter;

    final void collectInput() {
        if (everyInputCollected()) {
            triggerConverge();
        }
    }

    public final void triggerConverge(){
        Flow flow = converge(inputs.stream());
        outputs.forEach(output -> output.accept(flow));
        backpass(outputs.stream());
    }

    public final int inputSize() {
        return inputs.size();
    }

    public final int outputSize() {
        return outputs.size();
    }

    protected boolean everyInputCollected() {
        if (collectedInputCounter++ == inputs.size()) {
            collectedInputCounter = 0;
            return true;
        }
        return false;
    }

    public abstract Flow converge(Stream<T> stream);

    public abstract void backpass(Stream<U> stream);

    public final void addInput(T t){
        assert !inputs.contains(t);
        inputs.add(t);
    }

    public final void addOutput(U u){
        assert !outputs.contains(u);
        outputs.add(u);
    }

}
