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
