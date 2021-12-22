package neural;

import core.*;

public class Synapse<T extends Node<? extends FlowSupplier, ? extends FlowConsumer>, U extends Node<? extends FlowSupplier, ? extends FlowConsumer>> extends Edge<T, U> {

    public Synapse(T input, U output) {
        super(input, output);
    }
}
