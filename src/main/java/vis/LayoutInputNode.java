package vis;

import core.Node;
import neural.Synapse;

public interface LayoutInputNode extends Identifiable {

    static LayoutInputNode of(LayoutInnerNode innerNode) {
        return new LayoutInputNode() {
            @Override
            public int getTotalOutputs() {
                return innerNode.getTotalOutputs();
            }

            @Override
            public int getPositiveOutputs() {
                return innerNode.getPositiveOutputs();
            }

            @Override
            public int getNegativeOutputs() {
                return innerNode.getNegativeOutputs();
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

            @Override
            public String getUiClass() {
                return node.getClass().getSimpleName().toLowerCase();
            }
        };
    }

    String getUiClass();
}
