package neural;

import core.Flow;
import core.FlowSupplier;

import java.util.function.BooleanSupplier;

public class Reception implements FlowSupplier {
    private final BooleanSupplier booleanSupplier;

    public Reception(BooleanSupplier booleanSupplier) {
        this.booleanSupplier = booleanSupplier;
    }

    @Override
    public void acceptBackward(Flow flow) {

    }

    @Override
    public Flow getForward() {
        return Flow.from(booleanSupplier);
    }
}
