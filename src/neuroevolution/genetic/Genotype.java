package neuroevolution.genetic;

import java.util.ArrayList;
import java.util.List;

import game.factory.BirdFactory;
import game.model.Bird;
import neuroevolution.neuralnetwork.NeuralNetwork;

public class Genotype {

	public Bird bird;
	public float fitness;
	
	public Genotype() {
		this.bird = BirdFactory.getBird();
		this.fitness = 0;
	}
	
	public Genotype(NeuralNetwork.FlattenNetwork net) {
		this.bird = BirdFactory.getBird(net);
		this.fitness = 0;
	}
	
	public Genotype(Genotype genome) {
		this.bird = BirdFactory.getBird(genome.bird.net.flatten());
		this.fitness = 0;
	}
	
	public static List<Genotype> breed(Genotype male, Genotype female, int childCount, float mutationRate, float mutationStdDev) {
		List<Genotype> children = new ArrayList<Genotype>();
		for (int ch = 0; ch < childCount; ch++) {
			NeuralNetwork.FlattenNetwork childNet = male.bird.net.flatten();
			NeuralNetwork.FlattenNetwork parentNet = female.bird.net.flatten();
			for (int i = 0; i < childNet.weights.size(); i++) {
				if (Math.random() <= 0.5) {
					childNet.weights.set(i, parentNet.weights.get(i));
				}
			}
			for (int i = 0; i < childNet.weights.size(); i++) {
				if (Math.random() <= mutationRate) {
					childNet.weights.set(i, (float) Math.random()*2*mutationStdDev - mutationStdDev);
				}
			}
			children.add(new Genotype(childNet));
		}
		return children;
	}
}
