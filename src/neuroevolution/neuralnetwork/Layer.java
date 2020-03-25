/*
Using artifical neural network and genetic algorithm to train bot to play Flappy Bird
Copyright (C) 2020 Dušan Erdeljan

This file is part of neuroevolution-flappy-bird

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>
*/

package neuroevolution.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class Layer {

	public List<Neuron> neurons;
	
	private Layer() {
		this.neurons = new ArrayList<Neuron>();
	}
	
	public Layer(int neuronCount, int numInputs) {
		this();
		for (int i = 0; i < neuronCount; i++) {
			this.neurons.add(new Neuron(numInputs));
		}
	}
	
	public void eval(Layer prevLayer) {
		for (Neuron neuron: this.neurons) {
			float weightedSum = 0.0f;
			for (int i = 0; i < prevLayer.neurons.size(); i++) {
				Neuron prevNeuron = prevLayer.neurons.get(i);
				weightedSum += prevNeuron.value * neuron.weights.get(i);
			}
			neuron.value = this.activate(weightedSum);
		}
	}
	
	public float[] getOutput() {
		float[] output = new float[this.neurons.size()];
		for (int i = 0; i < this.neurons.size(); i++) {
			output[i] = this.neurons.get(i).value;
		}
		return output;
	}
	
	private float activate(float weightedSum) {
		return (float) (1 / (1 + Math.exp(-weightedSum)));
	}
}
