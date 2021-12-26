package neural;

import core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public class Network implements NodeEventListener {

    private final List<Receptor> receptors = new ArrayList<>();
    private final List<Effector> effectors = new ArrayList<>();

    static Flow convergeForward(Stream<? extends Synapse<?, ?>> stream) {
        if (stream.anyMatch(s -> s.getForward() == Flow.RUN && s.getType() == Synapse.Type.INHIBITORY)) {
            return Flow.STILL;
        } else if (stream.anyMatch(s -> s.getForward() == Flow.RUN && s.getType() == Synapse.Type.EXCITATORY)) {
            return Flow.RUN;
        } else {
            return Flow.STILL;
        }
    }

    public static Flow convergeBackward(Stream<? extends Synapse<?, ?>> stream) {
        return null;
    }

    public void addReceptor(BooleanSupplier booleanSupplier) {
        receptors.add(new Receptor(this, Objects.requireNonNull(booleanSupplier)));
    }

    public void addEffector(Runnable runnable) {
        effectors.add(new Effector(this, Objects.requireNonNull(runnable)));
    }

    private Neuron addNeuron() {
        return new Neuron(this);
    }

    public void tick() {
        receptors.forEach(Node::triggerConverge);
        effectors.forEach(Node::triggerBackpass);
    }

    @Override
    public Flow onFlowsCrossed(Node<?, ?> node, Flow forwardFlow, Flow backwardFlow) {
        return null;
    }

}
