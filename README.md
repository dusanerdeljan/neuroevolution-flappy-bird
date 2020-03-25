# neuroevolution-flappy-bird
Using artificial neural network and genetic algorithm to train bot to play Flappy Bird.

## Neural network architecture

Neural network used is simple neural network with 3 inputs, 8 hidden neurons and 2 output neurons.<br/>
Inputs to the network are:
  * Horizontal distance from the bird to the center of the closest pipe
  * Vertical distance to the top of the gap of the closest pipe
  * Vertical distance to the bottom of the gap of the closest pipe
  
Net has two outputs, bird will flap if the first output is greater than the second

## Genetic algorithm options

 * Population size: 100
 * Elitism: 0.2
 * Random behaviour: 0.2
 * Mutation rate: 0.1
 * Mutation standard deviation: 0.5
 * Number of children: 1
 
### Neural network encoding

Each neural network flattens to one-dimensional array of weights. First elements of the array are the weights connecting input layer with the first hidden layer and so on.

### Crossover

Child firstly takes all the weights from one of its parents. For every weight of the child's network, there is a 50% chance for it to be replaced with the corresponding weight of the second parent's network.

### Selection

Selection algorithm used is Pool selection.

## Graphics library

Java Processing 3.5.4

## License

This program is free.</br>
You can redistribute it and/or change it under the terms of **GNU General Public License version 3.0** (GPLv3). </br>
You can find a copy of the license in the repository.
