package vis;

import neural.Neuron;
import neural.Synapse;

public interface LayoutInnerNode {

    static LayoutInnerNode of(Neuron neuron) {
        return new LayoutInnerNode() {
            @Override
            public int getId() {
                return neuron.getId();
            }

            @Override
            public int getTotalOutputs() {
                return neuron.outputSize();
            }

            @Override
            public int getPositiveOutputs() {
                return (int) neuron.streamOfOutputs()
                        .filter(s -> s.getType() == Synapse.Type.EXCITATORY)
                        .count();
            }

            @Override
            public int getNegativeOutputs() {
                return (int) neuron.streamOfOutputs()
                        .filter(s -> s.getType() == Synapse.Type.INHIBITORY)
                        .count();
            }

            @Override
            public String getUiClass() {
                return neuron.getClass().getSimpleName().toLowerCase();
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

    int getTotalInputs();

    int getPositiveInputs();

    int getNegativeInputs();

    int getId();

    int getTotalOutputs();

    int getPositiveOutputs();

    int getNegativeOutputs();

    String getUiClass();
}
