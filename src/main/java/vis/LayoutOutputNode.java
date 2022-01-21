package vis;

import core.Node;
import neural.Synapse;

public interface LayoutOutputNode extends Identifiable {

    int getTotalInputs();

    int getPositiveInputs();

    int getNegativeInputs();

    String getUiClass();

    static LayoutOutputNode of(LayoutInnerNode innerNode) {
        return new LayoutOutputNode() {
            @Override
            public int getTotalInputs() {
                return innerNode.getTotalInputs();
            }

            @Override
            public int getPositiveInputs() {
                return innerNode.getPositiveInputs();
            }

            @Override
            public int getNegativeInputs() {
                return innerNode.getNegativeInputs();
            }

            @Override
            public String getUiClass() {
                return innerNode.getUiClass();
            }

            @Override
            public int getId() {
                return innerNode.getId();
            }
        };
    }

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

            @Override
            public String getUiClass() {
                return neuron.getClass().getSimpleName().toLowerCase();
            }
        };
    }
}
