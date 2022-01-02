# Self-organizing AI (RL)
This is Java lab for developing and experimenting with brand-new AI technology.
The technology is fully my own and hadn't been published anywhere.
Core code is non-commercial for now and will always be non-commercial in future while commercial product based on it are feasible.

# ✨ Priceless thousand Source Lines of code ✨

While the core logic certainly will have few SLOC the abilities are expected to be very wide and powerful.

### Tags (technologies) which the project is relevant to:

| Tag | Description |
| --- | --- |
| Neural network | Here are Receptor, Effector and Neuron abstractions. |
| [Self-organization](https://en.wikipedia.org/wiki/Self-organization) | [Local rules](https://en.wikipedia.org/wiki/Cellular_automaton) only are applied while the network grows. |
| [Reinforcement learning](https://en.wikipedia.org/wiki/Reinforcement_learning) | No supervisor at all. True [Black Box abstraction](https://en.wikipedia.org/wiki/Black_box). |
| Structural adaptation | The network adjusts its structure adapting to the input signals. |
| Mathless approach | There is no math at all used in algorithms on network grow. |

### Potential applications:

- Zoo of [AI applications](https://en.wikipedia.org/wiki/Applications_of_artificial_intelligence)

## Current state

Rewriting the original Self-organized Neural Network pet project from the scratch. Fine-tuning core logic now.

Proof-of-concept, Single-threaded and Non-optimized.

It has visualization now!

![Alt text](src/main/resources/git/scr1.jpg?raw=true "Title")

## How to start with it

- Clone the project. Open it in your favorite IDE.
- Start exploring [Playground package](https://github.com/sturex/sonn/tree/master/src/main/java/playground).
- or create your own class with main method with contents as below



``` java
Random random = new Random();
Network network = new Network();

network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));

network.addReceptor(random::nextBoolean);
network.addReceptor(random::nextBoolean);
network.addReceptor(random::nextBoolean);
network.addReceptor(random::nextBoolean);

for (int idx = 0; idx < 20; idx++) {
    network.tick();
    Thread.sleep(50);
}
```

## Near or far future steps
After polishing the core I will also add rwa (Real-World Applications) package with some approaches to common AI problems
- [Anomaly detection](https://en.wikipedia.org/wiki/Anomaly_detection) on **multivariate data**
- Pattern recognition on **multivariate time-series** data with **noise**
- True [reinforcement learning](https://en.wikipedia.org/wiki/Reinforcement_learning) based approach to training unmanned vehicles, [UAV](https://en.wikipedia.org/wiki/Unmanned_aerial_vehicle) or [UGV](https://en.wikipedia.org/wiki/Unmanned_ground_vehicle) etc.
