package core;

import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public enum Flow {
    RUN, STILL;

    public static Flow of(boolean b) {
        return b ? RUN : STILL;
    }

    public static Flow from(BooleanSupplier supplier) {
        return of(supplier.getAsBoolean());
    }

    public static Flow converge(Stream<FlowSupplier> stream) {
        return stream.anyMatch(f -> f.get() == Flow.RUN) ? Flow.RUN : Flow.STILL;
    }

    public void start(Runnable runnable) {
        if (this == RUN) {
            runnable.run();
        }
    }
}
