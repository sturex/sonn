package vis;

import core.Node;
import neural.Synapse;

public interface LayoutInputNode extends Identifiable {

    int getTotalOutputs();

    int getPositiveOutputs();

    int getNegativeOutputs();

    static <T extends Node<?, ? extends Synapse<?, ?>>> LayoutInputNode of(T node) {
        return new LayoutInputNode() {

            @Override
            public int getId() {
                return node.getId();
            }

            @Override
            public int getTotalOutputs() {
                return node.outputSize();
            }

            @Override
            public int getPositiveOutputs() {
                return (int) node.streamOfOutputs()
                        .filter(s -> s.getType() == Synapse.Type.EXCITATORY)
                        .count();
            }

            @Override
            public int getNegativeOutputs() {
                return (int) node.streamOfOutputs()
                        .filter(s -> s.getType() == Synapse.Type.INHIBITORY)
                        .count();
            }
        };
    }
}
