package neural;

import core.Flow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

class NeuronTest {

    private final BooleanSupplier trueSupplier = () -> true;
    private final BooleanSupplier falseSupplier = () -> false;

    @Test
    @DisplayName("Neuron correctly converges different flows from inputs through EXCITATORY synapses")
    void convergeExcitatorySynapsesTest() {
        Neuron neuron = new Neuron();
        Receptor r1 = new Receptor(trueSupplier);
        Receptor r2 = new Receptor(falseSupplier);
        Network.connect(r1, neuron, Synapse.Type.EXCITATORY);
        Network.connect(r2, neuron, Synapse.Type.EXCITATORY);
        r1.triggerConverge();
        r2.triggerConverge();
        assertEquals(Flow.RUN, neuron.getForwardFlow());
    }

    @Test
    @DisplayName("Neuron correctly converges different flows from inputs through INHIBITORY synapses")
    void convergeInhibitorySynapsesTest() {
        Neuron neuron = new Neuron();
        Receptor r1 = new Receptor(trueSupplier);
        Receptor r2 = new Receptor(falseSupplier);
        Network.connect(r1, neuron, Synapse.Type.INHIBITORY);
        Network.connect(r2, neuron, Synapse.Type.INHIBITORY);
        r1.triggerConverge();
        r2.triggerConverge();
        assertEquals(Flow.STILL, neuron.getForwardFlow());
    }

    @Test
    @DisplayName("Neuron correctly converges different flows from inputs through different synapses")
    void convergeInhibitoryAndExcitatorySynapsesTest() {
        Neuron neuron = new Neuron();
        Receptor r1 = new Receptor(trueSupplier);
        Receptor r2 = new Receptor(falseSupplier);
        Receptor r3 = new Receptor(trueSupplier);
        Receptor r4 = new Receptor(falseSupplier);
        Network.connect(r1, neuron, Synapse.Type.INHIBITORY);
        Network.connect(r2, neuron, Synapse.Type.EXCITATORY);
        Network.connect(r3, neuron, Synapse.Type.INHIBITORY);
        Network.connect(r4, neuron, Synapse.Type.EXCITATORY);
        r1.triggerConverge();
        r2.triggerConverge();
        r3.triggerConverge();
        r4.triggerConverge();
        assertEquals(Flow.STILL, neuron.getForwardFlow());
    }


    @Test
    @DisplayName("Neuron correctly converges different flows from inputs through different synapses 2.")
    void convergeInhibitoryAndExcitatorySynapsesTest2() {
        Neuron neuron = new Neuron();
        Receptor r1 = new Receptor(trueSupplier);
        Receptor r2 = new Receptor(falseSupplier);
        Receptor r3 = new Receptor(falseSupplier);
        Network.connect(r1, neuron, Synapse.Type.EXCITATORY);
        Network.connect(r2, neuron, Synapse.Type.EXCITATORY);
        Network.connect(r3, neuron, Synapse.Type.INHIBITORY);
        r1.triggerConverge();
        r2.triggerConverge();
        r3.triggerConverge();
        assertEquals(Flow.RUN, neuron.getForwardFlow());
    }

}