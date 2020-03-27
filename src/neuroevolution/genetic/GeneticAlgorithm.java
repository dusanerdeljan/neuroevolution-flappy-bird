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

import java.util.List;

import game.factory.BirdFactory;
import game.model.Pipe;
import neuroevolution.neuralnetwork.NeuralNetwork;
import util.Screen;

public class GeneticAlgorithm {
	
	private class PipeInfo {
		public float distance;
		public Pipe closestPipe;
		
		public PipeInfo(float distance, Pipe closestPipe) {
			this.distance = distance;
			this.closestPipe = closestPipe;
		}
	}

	public Population population;
	public int alive;
	public int generation;
	
	public int populationSize = 100;
	public float elitism = 0.2f;
	public float mutationRate = 0.1f;
	public float mutationStdDev = 0.5f;
	public float randomness = 0.2f;
	public int childCount = 1;
	
	private NeuralNetwork bestGenome;
	
	public GeneticAlgorithm() {
		this.population = new Population(this.populationSize);
		this.bestGenome = this.population.genomes.get(0).bird.net;
		this.alive = this.populationSize;
		this.generation = 1;
	}
	
	public void updatePopulation(List<Pipe> pipes) {
		PipeInfo data = getClosestPipe(pipes);
		for (Genotype genome: this.population.genomes) {
			if (!genome.bird.isDead) {
				genome.bird.feed(data.closestPipe, data.distance);
				genome.bird.update();
				if (genome.bird.y < genome.bird.radius || genome.bird.y > Screen.HEIGHT-genome.bird.radius) {
					genome.bird.isDead = true;
					this.alive--;
				}
			}
		}
	}
	
	public void evolvePopulation() {
		this.alive = this.populationSize;
		this.generation++;
		this.population.evolve(this.elitism, this.randomness, this.mutationRate, this.mutationStdDev, this.childCount);
		this.bestGenome = this.population.genomes.get(0).bird.net;
	}
	
	public NeuralNetwork getBestGenome() {
		return this.bestGenome;
	}
	
	public int getBestScore() {
		int best = 0;
		for (Genotype genome: this.population.genomes) {
			if (genome.bird.gameScore > best) {
				best = genome.bird.gameScore;
			}
		}
		return best;
	}
	
	public boolean populationDead() {
		for (Genotype genome: this.population.genomes) {
			if (!genome.bird.isDead) {
				return false;
			}
		}
		return true;
	}
	
	private PipeInfo getClosestPipe(List<Pipe> pipes) {
		Pipe closestPipe = null;
		float distance = Float.MAX_VALUE;
		for (Pipe pipe: pipes) {
			float test = pipe.x + pipe.width/2 - BirdFactory.getSpawnX();
			if (Math.abs(test) < Math.abs(distance)) {
				distance = test;
				closestPipe = pipe;
			}
		}
		return new PipeInfo(distance, closestPipe);
	}
}
