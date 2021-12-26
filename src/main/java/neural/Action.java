package neural;

import core.Flow;
import core.FlowConsumer;

public class Action implements FlowConsumer {
    private final Runnable runnable;
    private Flow backwardFlow = Flow.STILL;

    public Action(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void acceptForward(Flow flow) {
        backwardFlow = flow;
        flow.start(runnable);
    }

    @Override
    public Flow getBackward() {
        return backwardFlow;
    }
}
