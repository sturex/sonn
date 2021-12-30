package neural;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing grow of the network on certain input flows")
class NetworkTest {

    @Test
    @DisplayName("It grows correctly with single receptor and no effectors")
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

        network.addReceptor(queue::poll);

        while (!queue.isEmpty()){
            network.tick();
        }

        assertEquals(8, synapses.size());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(0).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(1).getType());
        assertEquals(Synapse.Type.INHIBITORY, synapses.get(2).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(3).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(4).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(5).getType());
        assertEquals(Synapse.Type.EXCITATORY, synapses.get(6).getType());
    }
}