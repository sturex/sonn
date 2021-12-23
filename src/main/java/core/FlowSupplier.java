package core;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public interface FlowSupplier extends Supplier<Flow> {

    static FlowSupplier of(BooleanSupplier booleanSupplier) {
        return booleanSupplier.getAsBoolean() ? () -> Flow.RUN : () -> Flow.STILL;
    }
}
