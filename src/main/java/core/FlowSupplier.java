package core;

import java.util.function.BooleanSupplier;

public interface FlowSupplier {

    void acceptBackward(Flow flow);

    Flow getForward();
}
