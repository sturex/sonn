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
        if (type == Type.EXCITATORY) {
            setForwardFlow(Flow.RUN);
        }
    }

    @Override
    protected Flow combineForwardFlow(Flow forwardFlow, Flow backwardFlow, Flow acceptedFlow) {
        if (isExcitatory() && forwardFlow == Flow.RUN) {
            return forwardFlow;
        } else {
            return acceptedFlow;
        }
    }

    @Override
    protected Flow combineBackwardFlow(Flow forwardFlow, Flow backwardFlow, Flow acceptedFlow) {
        if (acceptedFlow == Flow.RUN) {
            setForwardFlow(Flow.STILL);
        }
        return acceptedFlow;
    }

    private boolean isExcitatory() {
        return type == Type.EXCITATORY;
    }

    private boolean isInhibitory() {
        return type == Type.INHIBITORY;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "S" + getId() + "-" + type.toString().charAt(0) + " (" + getForward() + "-" + getBackward() + "): " + getInput() + " -> " + getOutput();
    }
}
