package core;

public interface FlowConsumer {
    void acceptForward(Flow flow);

    Flow getBackward();
}
