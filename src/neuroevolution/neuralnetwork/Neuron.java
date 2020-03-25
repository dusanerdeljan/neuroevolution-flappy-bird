package neuroevolution.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
	
	public float value;
	public List<Float> weights;
	
	public Neuron(int weightCount) {
		this.value = 0;
		this.weights = new ArrayList<Float>();
		for (int i = 0; i < weightCount; i++) {
			this.weights.add((float) (Math.random() * 2 - 1));
		}
	}
}
