package core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class Node<T extends FlowSupplier, U extends FlowConsumer> {

    private final List<T> inputs = new ArrayList<>();
    private final List<U> outputs = new ArrayList<>();
    private long collectedInputCounter;

    public final void forEachOutput(Consumer<U> uConsumer) {
        outputs.forEach(uConsumer);
    }

    final void collectInput() {
        if (everyInputCollected()) {
            triggerRelease();
        }
    }

    public final void triggerRelease(){
        release(inputs.stream());
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

    public abstract void release(Stream<T> stream);

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
