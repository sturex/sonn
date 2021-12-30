package core;

import neural.Action;
import neural.Reception;

import java.util.List;
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

    public static Flow convergeForward(List<Reception> receptions) {
        return receptions.stream().anyMatch(f -> f.getForward() == Flow.RUN) ? Flow.RUN : Flow.STILL;
    }

    public static Flow convergeBackward(List<Action> actions) {
        return actions.stream().anyMatch(f -> f.getBackward() == Flow.RUN) ? Flow.RUN : Flow.STILL;
    }

    public void start(Runnable runnable) {
        if (this == RUN) {
            runnable.run();
        }
    }
}
