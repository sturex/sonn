package neural;

import core.Flow;
import core.FlowConsumer;

public class Action implements FlowConsumer {
    private final Runnable runnable;
    private Flow forwardFlow = Flow.STILL;
    private boolean isPunished = false;

    public boolean isPunished() {
        return isPunished;
    }

    public void setPunished(boolean punished) {
        isPunished = punished;
    }

    public Action(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void acceptForward(Flow flow) {
        forwardFlow = flow;
        flow.start(runnable);
    }

    @Override
    public Flow getBackward() {
        return isPunished ? Flow.STILL : forwardFlow;
    }
}
