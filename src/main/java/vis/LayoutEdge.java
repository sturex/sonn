package vis;

import neural.Synapse;

public interface LayoutEdge {

    int getSourceId();

    int getTargetId();

    static LayoutEdge of(Synapse<?, ?> synapse) {

        return new LayoutEdge() {
            @Override
            public int getSourceId() {
                return synapse.getInput().getId();
            }

            @Override
            public int getTargetId() {
                return synapse.getOutput().getId();
            }

            @Override
            public String getUiClass() {
                String bypassed = synapse.isForwardRun() ? "bypassed" : "still";
                return bypassed + "_" + synapse.getType().name().toLowerCase();
            }
        };
    }

    String getUiClass();
}
