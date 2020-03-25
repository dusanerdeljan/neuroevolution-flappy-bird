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

import neuroevolution.neuralnetwork.NeuralNetwork;

public class Population {

	public List<Genotype> genomes;
	
	public Population(int populationSize) {
		this.genomes = new ArrayList<Genotype>();
		for (int i = 0; i < populationSize; i++) {
			this.genomes.add(new Genotype());
		}
	}
	
	public void evolve(float elitism, float randomness, float mutationRate, float mutationStdDev, int childCount) {
		this.normalFitnessDistribution();
		this.sortByFitness();
		List<Genotype> nextGeneration = new ArrayList<Genotype>();
		int eliteCount = Math.round(elitism*this.genomes.size());
		for (int i = 0; i < eliteCount; i++) {
			nextGeneration.add(new Genotype(this.genomes.get(i)));
		}
		int randomCount = Math.round(randomness*this.genomes.size());
		for (int i = 0; i < randomCount; i++) {
			NeuralNetwork.FlattenNetwork net  = this.genomes.get(0).bird.net.flatten();
			for (int j = 1; j < net.weights.size(); j++) {
				net.weights.set(j, (float) (Math.random()*2 - 1));
			}
			nextGeneration.add(new Genotype(net));
		}
		// Pool selection
		int max = 0;
		while (true) {
			for (int i = 0; i < max; i++) {
				List<Genotype> children = Genotype.breed(this.genomes.get(i), this.genomes.get(max), childCount, mutationRate, mutationStdDev);
				for (Genotype child: children) {
					nextGeneration.add(child);
					if (nextGeneration.size() >= this.genomes.size()) {
						this.genomes = nextGeneration;
						return;
					}
				}
			}
			max++;
			max = max >= this.genomes.size()-1 ? 0 : max;
		}
	}

	private void normalFitnessDistribution() {
		float sum = 0f;
		for (Genotype genome: this.genomes) {
			sum += genome.bird.score;
		}
		for (Genotype genome: this.genomes) {
			genome.fitness = genome.bird.score / sum;
		}
	}
	
	// TODO: Implement quick sort or something else 
	private void sortByFitness() {
		for (int i = 0; i < this.genomes.size()-1; i++) {
			int bestIndex = i;
			for (int j = i+1; j < this.genomes.size(); j++) {
				if (this.genomes.get(j).fitness > this.genomes.get(bestIndex).fitness) {
					bestIndex = j;
				}
			}
			if (bestIndex != i) {
				Genotype temp = this.genomes.get(bestIndex);
				this.genomes.set(bestIndex, this.genomes.get(i));
				this.genomes.set(i, temp);
			}
		}
	}
}
