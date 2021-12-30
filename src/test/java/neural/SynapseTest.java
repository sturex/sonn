package neural;

import core.Flow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Synapse tests")
class SynapseTest {

    @Test
    @DisplayName("Synapse correctly accepts forward flow")
    void forwardFlowExcSynapseTest() {

        Receptor receptor = new Receptor(() -> true);
        Effector effector = new Effector(() -> {
        });

        Arrays.stream(Synapse.Type.values())
                .map(t -> new Synapse<>(receptor, effector, t))
                .forEach(s -> Arrays.stream(Flow.values())
                        .forEach(flow -> {
                            s.acceptForward(flow);
                            assertEquals(flow, s.getForward());
                            assertEquals(Flow.STILL, s.getBackward());
                        }));
    }

    @Test
    @DisplayName("Synapse correctly accepts backward flow")
    void backwardFlowExcSynapseTest() {

        Receptor receptor = new Receptor(() -> true);
        Effector effector = new Effector(() -> {
        });

        Arrays.stream(Synapse.Type.values())
                .map(t -> new Synapse<>(receptor, effector, t))
                .forEach(s -> Arrays.stream(Flow.values())
                        .forEach(flow -> {
                            s.acceptBackward(flow);
                            assertEquals(flow, s.getBackward());
                            assertEquals(Flow.STILL, s.getForward());
                        }));
    }
}