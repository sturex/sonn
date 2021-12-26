package neural;

import core.Flow;
import core.FlowSupplier;

import java.util.function.BooleanSupplier;

public class Reception implements FlowSupplier {
    private final BooleanSupplier booleanSupplier;

    private Flow forwardFlow = Flow.STILL;

    public Reception(BooleanSupplier booleanSupplier) {
        this.booleanSupplier = booleanSupplier;
    }

    @Override
    public void acceptBackward(Flow flow) {

    }

    @Override
    public Flow getForward() {
        forwardFlow = Flow.from(booleanSupplier);
        return forwardFlow;
    }
}
