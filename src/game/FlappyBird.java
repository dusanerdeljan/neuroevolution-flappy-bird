package game;

import java.util.LinkedList;
import java.util.List;

import game.factory.PipeFactory;
import game.model.Bird;
import game.model.Pipe;
import neuroevolution.genetic.GeneticAlgorithm;
import neuroevolution.genetic.Genotype;
import processing.core.PApplet;
import processing.core.PImage;
import util.Screen;

public class FlappyBird extends PApplet {
	
	List<Pipe> pipes;
	GeneticAlgorithm agent;
	
	float pipeSwapnX;
	float pipeStart;
	int score = 0;
	
	int pipeSpawnRate;
	
	PImage backgroundImage;
	
	public void settings() {
		size(1366, 768);
		Screen.setDimensions(width, height);
		this.pipeSwapnX = width;
		this.pipeStart = width / 3f;
		this.agent = new GeneticAlgorithm();
		this.pipes = new LinkedList<Pipe>();
		spawnPipe(pipeStart);
		spawnPipe(2*pipeStart);
		spawnPipe(3*pipeStart);
		this.pipeSpawnRate = Math.abs(Math.round(pipeStart / this.pipes.get(0).velocity));
	}
	
	public void setup() {
		backgroundImage = loadImage("resources/background.png");
		surface.setTitle("Neuroevolution Flappy Bird");
	}
	
	public void draw() {
		clearScreen();
		agent.population.genomes.forEach(genome -> renderBird(genome.bird));
		pipes.forEach(pipe -> renderPipe(pipe));
		drawGenerationInfo();
		pipes.removeIf(pipe -> pipe.isInvisible());
		for (Genotype genome: this.agent.population.genomes) {
			for (Pipe pipe: this.pipes) {
				checkCollision(pipe, genome.bird);
			}
		}
		if (frameCount % pipeSpawnRate == 0) {
			spawnPipe(this.pipeSwapnX);
		}
		pipes.forEach(pipe -> pipe.update());
		agent.updatePopulation(pipes);
		if (agent.populationDead()) {
			reset();
		}
	}
	
	private void drawGenerationInfo() {
		textSize(32);
		fill(255);
		text("Score: " + agent.getBestScore(), 20, 50);
		text("Generation: " + agent.generation, 20, 100);
		text("Alive: " + agent.alive + " / " + agent.populationSize, 20, 150);
	}
	
	private void checkCollision(Pipe pipe, Bird bird) {
		if (bird.isDead)
			return;
		if (pipe.checkBirdCollision(bird)) {
			bird.isDead = true;
			agent.alive--;
		}
	}
	
	private void reset() {
		pipes.clear();
		spawnPipe(pipeStart);
		spawnPipe(2*pipeStart);
		spawnPipe(3*pipeStart);
		frameCount = 0;
		score = 0;
		agent.evolvePopulation();
	}
	
	private void spawnPipe(float x) {
		pipes.add(PipeFactory.getPype(x));
	}
	
	private void clearScreen() {
		image(backgroundImage, 0, 0);
	}

	private void renderBird(Bird bird) {
		if (!bird.isDead) {
			stroke(0);
			strokeWeight(1);
			fill(224, 227, 20);
			ellipseMode(CENTER);
			ellipse(bird.x, bird.y, bird.radius*2, bird.radius*2);	
		}
	}
	
	private void renderPipe(Pipe pipe) {
		stroke(0);
		strokeWeight(1);
		fill(9, 148, 46);
		rect(pipe.x, 0, pipe.width, height - pipe.height - pipe.gap);
		rect(pipe.x, 768 - pipe.height, pipe.width, pipe.height);
	}
}
