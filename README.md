# Self-organizing AI (RL)
This is Java lab for developing and experimenting with brand-new AI technology.
The technology is fully my own and hadn't been published anywhere.
Core code is non-commercial for now and will always be non-commercial in future while commercial product based on it are feasible.

## How it works

The idea is based on principles of self-organization where the network structure adaptively grows by interfering between previous and current states shapshots. It has long-term memory written in network structure and short-term memory written in nodes and edges states. 

The model does not use complex math formulas for a neuron or synapse behavior. All computations are performed on graph where each node acts like a gate to the input abstract flow which is divisible but equal.

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

## Visualization

I use awesome [GraphStream](https://graphstream-project.org/) library for layout of the network.
Visualization is aimed mostly for development and showcase purposes. 

Green color is for **Excitatory** synapses, Red color for **Inhibitory** ones. 

Static and Dynamic layouts can be applied as follows

``` java 
List<NetworkEventsListener> listeners = List.of(
        new LayoutAdapter(new GraphStreamStaticLayout()),
        new LayoutAdapter(new GraphStreamDynamicLayout()));
Network network = new Network(listeners, 400);
```
### Static layout
It just an adjacency matrix of underlying directed graph. The **Flow** (core concept expression) passes from bottom to left side of matrix. 
The circles are neurons aka nodes. The squares stay for edges.
Static layout has an event-based methods for to react to Flow bypassing through nodes and edges - 
this is why different squares opaqueness and circles sizes are happened.

![Alt text](src/main/resources/git/scr1.jpg?raw=true "Static layout")

### Dynamic layout

Below is the basic example on how the network grows using GraphStream's out-of-the-box dynamic layout. Note, the recording shows a very simple graph layout while complex ones are totally unreadable and useless. 

[![Everything Is AWESOME](https://yt-embed.herokuapp.com/embed?v=a9dRjU2J7Ag)](https://youtu.be/a9dRjU2J7Ag "Self-Organizing Neural Network")

## Project state
Proof-of-concept, Single-threaded, Non-optimized, Ugly sometimes


## Board
Next milestone: **Reinforcement learning and associative formation are fine-tuned**

### Done
- The original Self-organized Neural Network pet project rewritten from the scratch
  - Abstract Nodes and Edges, Graph draft. Check the [Core package](https://github.com/sturex/sonn/tree/master/src/main/java/core)
  - Concrete Receptor, Effector, PainEffector, Neuron (aka hidden network unit) in [Neural package](https://github.com/sturex/sonn/tree/master/src/main/java/neural)
- Network self-organized growing
  - ⚠️Implemented an ugly workaround solution in core - should fix it
- [Associative memory](https://en.wikipedia.org/wiki/Associative_memory_(psychology)) formation using bio-inspired Reflexes.
- Different kinds of Receptor. See [Network code](https://github.com/sturex/sonn/blob/master/src/main/java/neural/Network.java)
  - basic reception for any single Object
  - reception for set of objects bounded to specific receptor (strict or dictionary)
  - adaptive (auto-extensible, on-demand) reception
  - floating point values bucketing
- [PainEffector](https://github.com/sturex/sonn/blob/master/src/main/java/neural/PainEffector.java) as core mechanism to [Reinforcement Learning paradigm](https://en.wikipedia.org/wiki/Reinforcement_learning) implementation 
- Event-driven Network visualization, see [Visualization package](https://github.com/sturex/sonn/tree/master/src/main/java/vis)
  - Static layout (adjacency matrix)
  - Dynamic layout (graph) supporting events from every subclass of Node 
- Real-world application samples
  - [Outlier detection](https://en.wikipedia.org/wiki/Anomaly_detection) on **multivariate data**. Code is [here](https://github.com/sturex/sonn/blob/master/src/main/java/samples/OutlierDetectionSample.java).
  - [Spatiotemporal](https://en.wikipedia.org/wiki/Spatiotemporal_pattern) Pattern recognition on **multivariate time-series** data with **noise**. Code is [here](https://github.com/sturex/sonn/blob/master/src/main/java/samples/PatternRecognitionSample.java)

### In progress

- Playground and Samples for
  - [Associative memory](https://en.wikipedia.org/wiki/Associative_memory_(psychology)) formation
  - Reinforcement learning
- Single pass Network (continuous) training
  - Supervised network constructing

### ToDo
- Wiki pages
- Add samples covering most of Real-world applications

## How to start with it

- Clone the project. Open it in your favorite IDE.
- Start exploring [Playground package](https://github.com/sturex/sonn/tree/master/src/main/java/playground) or [Samples package](https://github.com/sturex/sonn/tree/master/src/main/java/samples)
- or create your own class with main method with contents as below



``` java
Random random = new Random();
Network network = new Network();

network.addListener(new LayoutAdapter(new GraphStreamStaticLayout()));
network.addListener(new LayoutAdapter(new GraphStreamDynamicLayout()));

network.addReceptor(random::nextBoolean);
network.addReceptor(random::nextBoolean);
network.addReceptor(random::nextBoolean);
network.addReceptor(random::nextBoolean);

for (int idx = 0; idx < 20; idx++) {
    network.tick();
    Thread.sleep(50);
}
```

## Suddenly some words on AGI philosophy I profess
There is **_NO_** Intelligence in terms of condition we can measure. It's all about [natural selection](https://en.wikipedia.org/wiki/Natural_selection). 
Nature just eliminates all the species which do not conform to current conditions. We just observe symptoms calling it Intelligence.  

Everything that can accept _flow_ from outer environment then split it using itself inner structure and finally get it back to outer media is already intelligent. 
The nature of the [flow](https://github.com/sturex/sonn/blob/master/src/main/java/core/Flow.java) is irrelevant. 
It can be neurotransmitters and hormones or enumerated emotions each bounded to certain [Receptor](https://en.wikipedia.org/wiki/Sensory_neuron#Types_and_function) implemented in [Java code](https://github.com/sturex/sonn/blob/master/src/main/java/neural/Receptor.java).

## Contributing

I doubt you can realize the underlying technology with no articles describing it.
So just hold tight and wait 😀

## Contacts

Feel free to join to my contacts via [LinkedIn](https://www.linkedin.com/in/sturex/) or [Facebook](https://www.facebook.com/fbsturex)