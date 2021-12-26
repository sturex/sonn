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

    public static Flow convergeForward(Stream<? extends FlowSupplier> stream) {
        return stream.anyMatch(f -> f.getForward() == Flow.RUN) ? Flow.RUN : Flow.STILL;
    }

    public static Flow convergeBackward(Stream<? extends FlowConsumer> stream) {
        return stream.anyMatch(f -> f.getBackward() == Flow.RUN) ? Flow.STILL : Flow.RUN;
    }

    public void start(Runnable runnable) {
        if (this == RUN) {
            runnable.run();
        }
    }
}
