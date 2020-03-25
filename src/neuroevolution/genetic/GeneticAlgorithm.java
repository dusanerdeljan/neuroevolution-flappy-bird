package neuroevolution.genetic;

import java.util.List;

import game.factory.BirdFactory;
import game.model.Pipe;
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
	
	public GeneticAlgorithm() {
		this.population = new Population(this.populationSize);
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
	}
	
	public int getBestScore() {
		int best = 0;
		for (Genotype genome: this.population.genomes) {
			if (genome.bird.score > best) {
				best = genome.bird.score;
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
			float test = pipe.x - (BirdFactory.getSpawnX() + BirdFactory.getRadius());
			if (Math.abs(test) < Math.abs(distance)) {
				distance = test;
				closestPipe = pipe;
			}
		}
		return new PipeInfo(distance, closestPipe);
	}
}
