package core;

import java.util.function.BooleanSupplier;

public enum Flow {
    RUN, STILL;

    public static Flow of(boolean b) {
        return b ? RUN : STILL;
    }

    public static Flow from(BooleanSupplier supplier) {
        return of(supplier.getAsBoolean());
    }

    public void start(Runnable runnable) {
        if (this == RUN) {
            runnable.run();
        }
    }
}
