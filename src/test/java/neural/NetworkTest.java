package neural;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vis.GraphStreamStaticLayout;
import vis.LayoutAdapter;

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
        queue.add(true);

        network.addReceptor(queue::poll);

        while (!queue.isEmpty()){
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