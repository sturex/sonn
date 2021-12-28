package neural;

import core.*;

public class Synapse<T extends Node<?, ?>, U extends Node<?, ?>> extends Edge<T, U> implements FlowConsumer, FlowSupplier {

    Synapse(T input, U output, Type type) {
        super(input, output);
        this.type = type;
    }

    public enum Type {
        INHIBITORY, EXCITATORY
    }

    public Type getType() {
        return type;
    }

    private final Type type;

}
