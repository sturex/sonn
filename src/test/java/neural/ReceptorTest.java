package neural;

import core.Flow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

class ReceptorTest {

    private final BooleanSupplier trueSupplier = () -> true;
    private final BooleanSupplier falseSupplier = () -> false;

    @Test
    @DisplayName("Receptor converges TRUE supplement to RUN flow")
    void convergeTrueSupplementTest() {
        Receptor receptor = new Receptor(trueSupplier);
        receptor.triggerConverge();
        assertEquals(Flow.RUN, receptor.getForwardFlow());
    }

    @Test
    @DisplayName("Receptor converges FALSE supplement to STILL flow")
    void convergeFalseSupplementTest() {
        Receptor receptor = new Receptor(falseSupplier);
        receptor.triggerConverge();
        assertEquals(Flow.STILL, receptor.getForwardFlow());
    }

    @Test
    void convergeBackward() {

    }
}