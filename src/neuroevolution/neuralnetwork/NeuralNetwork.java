package neuroevolution.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
	
	public class FlattenNetwork {
		public List<Integer> neurons;
		public List<Float> weights;
		
		public FlattenNetwork() {
			this.neurons = new ArrayList<Integer>();
			this.weights = new ArrayList<Float>();
		}
	}

	public List<Layer> layers;
	
	private NeuralNetwork() {
		this.layers = new ArrayList<Layer>();
	}
	
	public NeuralNetwork(int... topolgy) {
		this();
		int prevInputs = 0;
		for (int i = 0; i < topolgy.length; i++) {
			this.layers.add(new Layer(topolgy[i], prevInputs));
			prevInputs = topolgy[i];
		}
	}
	
	public FlattenNetwork flatten() {
		FlattenNetwork net = new FlattenNetwork();
		for (Layer layer: this.layers) {
			net.neurons.add(layer.neurons.size());
			for (Neuron neuron: layer.neurons) {
				for (float weight: neuron.weights) {
					net.weights.add(weight);
				}
			}
		}
		return net;
	}
	
	public void expand(FlattenNetwork net) {
		this.layers.clear();
		int prevInput = 0;
		int weightIndex = 0;
		for (int neuronCount: net.neurons) {
			Layer layer = new Layer(neuronCount, prevInput);
			for (int i = 0; i < layer.neurons.size(); i++) {
				for (int j = 0; j < layer.neurons.get(i).weights.size(); j++) {
					layer.neurons.get(i).weights.set(j, net.weights.get(weightIndex++));
				}
			}
			prevInput = neuronCount;
			this.layers.add(layer);
		}
	}
	
	public float[] eval(float... inputs) {
		for (int i = 0; i < inputs.length; i++) {
			this.layers.get(0).neurons.get(i).value = inputs[i];
		}
		Layer prevLayer = this.layers.get(0);
		for (int i = 1; i < this.layers.size(); i++) {
			this.layers.get(i).eval(prevLayer);
			prevLayer = this.layers.get(i);
		}
		// prev layer is now the last layer in the network
		return prevLayer.getOutput();
	}
}
