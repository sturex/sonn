package vis;

import core.Node;
import neural.Synapse;

public interface LayoutOutputNode extends Identifiable {

    int getTotalInputs();

    int getPositiveInputs();

    int getNegativeInputs();

    static LayoutOutputNode of(Node<? extends Synapse<?, ?>, ?> neuron) {
        return new LayoutOutputNode() {
            @Override
            public int getId() {
                return neuron.getId();
            }

            @Override
            public int getTotalInputs() {
                return neuron.inputSize();
            }

            @Override
            public int getPositiveInputs() {
                return (int) neuron.streamOfInputs()
                        .filter(s -> s.getType() == Synapse.Type.EXCITATORY)
                        .count();
            }

            @Override
            public int getNegativeInputs() {
                return (int) neuron.streamOfInputs()
                        .filter(s -> s.getType() == Synapse.Type.INHIBITORY)
                        .count();
            }
        };
    }
}
