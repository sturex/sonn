package neural;

import core.*;

import java.util.List;
import java.util.function.BooleanSupplier;

public class Receptor extends Node<Reception, Synapse<Receptor, Neuron>> {

    protected Receptor(BooleanSupplier booleanSupplier) {
        addInput(new Reception(booleanSupplier));
    }

    @Override
    public Flow convergeForward(List<Reception> ts) {
        return Flow.convergeForward(ts);
    }

    @Override
    public Flow convergeBackward(List<Synapse<Receptor, Neuron>> us) {
        return Network.convergeBackward(us);
    }

    @Override
    public String toString() {
        return "R" + super.toString();
    }
}
