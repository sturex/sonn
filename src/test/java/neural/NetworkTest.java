package neural;

import core.Flow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

import java.util.*;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("Testing grow of the network on certain input flows")
class NetworkTest {

    private int target = 0;
    private final BooleanSupplier trueSupplier = () -> true;
    private final BooleanSupplier falseSupplier = () -> false;

    @Test
    @DisplayName("Primitive R-E chain with inhibitory synapse blocks input RUN flow")
    void chainWithInhibitorySynapseTest() {
        int expectedValue = 100;
        Receptor receptor = new Receptor(trueSupplier);
        Effector effector = new Effector(() -> target = expectedValue);
        Network.connect(receptor, effector, Synapse.Type.INHIBITORY);
        receptor.triggerConverge();
        assertEquals(Flow.RUN, receptor.getForwardFlow());
        assertEquals(Flow.STILL, effector.getForwardFlow());
        assertNotEquals(expectedValue, target);
    }

    @Test
    @DisplayName("Primitive R-E chain with excitatory synapse correctly transforms input RUN flow to output action")
    void chainWithExcitatorySynapseTest() {
        int expectedValue = 100;
        Receptor receptor = new Receptor(trueSupplier);
        Effector effector = new Effector(() -> target = expectedValue);
        Network.connect(receptor, effector, Synapse.Type.EXCITATORY);
        receptor.triggerConverge();
        assertEquals(Flow.RUN, receptor.getForwardFlow());
        assertEquals(Flow.RUN, effector.getForwardFlow());
        assertEquals(expectedValue, target);
    }

    @Test
    @DisplayName("Network grows correctly on predefined flow with single receptor and no effectors")
    void singleReceptorTest() {
        Network network = new Network();

        List<Synapse<?, ?>> synapses = new ArrayList<>();

        network.addListener(new NetworkEventsListener() {
            @Override
            public void onSynapseAdded(Synapse<?, ?> synapse) {
                synapses.add(synapse);
            }
        });

        Queue<Boolean> queue = new ArrayDeque<>();
        queue.add(true);
        queue.add(false);
        queue.add(true);
        queue.add(true);
        queue.add(false);
        queue.add(true);

        network.addReceptor(queue::poll);

        while (!queue.isEmpty()) {
            network.tick();
        }

        assertEquals(9, synapses.size());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(0).getType());
        assertEquals(Synapse.Type.INHIBITORY, synapses.get(1).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(2).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(3).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(4).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(5).getType());
        assertEquals(Synapse.Type.INHIBITORY, synapses.get(6).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(7).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(8).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(9).getType());
    }
}