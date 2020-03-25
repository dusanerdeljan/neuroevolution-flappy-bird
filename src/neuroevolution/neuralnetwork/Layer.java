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
