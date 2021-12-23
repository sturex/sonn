package neural;

import core.*;

class Synapse<T extends Node<? extends FlowSupplier, ? extends FlowConsumer>, U extends Node<? extends FlowSupplier, ? extends FlowConsumer>> extends Edge<T, U> {

    Synapse(T input, U output, Type type) {
        super(input, output);
        this.type = type;
    }

    public enum Type{
        INHIBITORY, EXCITATORY
    }

    public Type getType() {
        return type;
    }

    private final Type type;

}
