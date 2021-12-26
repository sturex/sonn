package core;

public interface NodeEventListener {

    Flow onFlowsCrossed(Node<?, ?> node, Flow forwardFlow, Flow backwardFlow);
}
