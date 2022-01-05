package neural;

import core.*;

public class Synapse<T extends Node<?, ?>, U extends Node<?, ?>> extends Edge<T, U> implements FlowConsumer, FlowSupplier {

    public enum Type {
        INHIBITORY, EXCITATORY
    }

    private final Type type;

    Synapse(T input, U output, Type type) {
        super(input, output);
        this.type = type;
        setBackwardFlow(Flow.RUN);
    }

    public Type getType() {
        return type;
    }

    @Override
    public void acceptForward(Flow flow) {
        if (getBackward() == Flow.RUN && type == Type.EXCITATORY) {
            super.acceptForward(Flow.RUN);
        } else {
            super.acceptForward(flow);
        }
    }

    @Override
    public void acceptBackward(Flow flow) {
        if (getForward() == Flow.RUN && type == Type.EXCITATORY) {
            super.acceptBackward(flow);
        } else {
            super.acceptBackward(flow);
        }
    }

    @Override
    public String toString() {
        return "S" + getId() + "-" + type.toString().charAt(0) + " (" + getForward() + "-" + getBackward() + "): " + getInput() + " -> " + getOutput();
    }
}
