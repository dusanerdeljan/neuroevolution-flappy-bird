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

package game;

import java.util.LinkedList;
import java.util.List;

import game.factory.BirdFactory;
import game.factory.PipeFactory;
import game.model.Bird;
import game.model.Pipe;
import neuroevolution.genetic.GeneticAlgorithm;
import neuroevolution.genetic.Genotype;
import neuroevolution.neuralnetwork.Layer;
import neuroevolution.neuralnetwork.NeuralNetwork;
import neuroevolution.neuralnetwork.Neuron;
import processing.core.PApplet;
import processing.core.PImage;
import util.Screen;

public class FlappyBird extends PApplet {
	
	List<Pipe> pipes;
	GeneticAlgorithm agent;
	
	float pipeSwapnX;
	float pipeStart;
	int score = 0;
	int highscore = 0;
	int tickCounter = 0;
	int speed = 1;
	int maxSpeed = 10;
	
	int pipeSpawnRate;
	
	PImage backgroundImage;
	
	public void settings() {
		size(1366, 768);
		Screen.setDimensions(width, height);
		BirdFactory.init(Screen.WIDTH/5.0f, Screen.HEIGHT/2.0f, 20);
		this.pipeSwapnX = width;
		this.pipeStart = width / 3f;
		this.agent = new GeneticAlgorithm();
		this.pipes = new LinkedList<Pipe>();
		this.initPipes();
		this.pipeSpawnRate = Math.abs(Math.round(pipeStart / this.pipes.get(0).velocity));
	}
	
	public void setup() {
		backgroundImage = loadImage("resources/background.png");
		surface.setTitle("Neuroevolution Flappy Bird");
	}
	
	public void draw() {
		for (int i = 0; i < speed; i++) {
			tickCounter++;
			clearScreen();
			agent.population.genomes.forEach(genome -> renderBird(genome.bird));
			pipes.forEach(pipe -> renderPipe(pipe));
			drawGenerationInfo();
			renderNeuralNetwork();
			pipes.removeIf(pipe -> pipe.isInvisible());
			for (Genotype genome: this.agent.population.genomes) {
				for (Pipe pipe: this.pipes) {
					checkCollision(pipe, genome.bird);
				}
			}
			if (tickCounter % pipeSpawnRate == 0) {
				spawnPipe(this.pipeSwapnX);
			}
			pipes.forEach(pipe -> pipe.update());
			agent.updatePopulation(pipes);
			if (agent.populationDead()) {
				reset();
			}
		}
	}
	
	public void keyPressed() {
		if (key == CODED) {
			if (keyCode == UP) {
				speed = min(maxSpeed, ++speed);
			} else if (keyCode == DOWN) {
				speed = max(1, --speed);
			}
		}
	}
	
	private void checkCollision(Pipe pipe, Bird bird) {
		if (bird.isDead)
			return;
		if (pipe.checkBirdCollision(bird)) {
			bird.isDead = true;
			agent.alive--;
		} else {
			if (pipe.checkPass(bird)) {
				bird.gameScore++;
			}
		}
	}
	
	private void reset() {
		pipes.clear();
		initPipes();
		frameCount = 0;
		tickCounter = 0;
		score = 0;
		agent.evolvePopulation();
	}
	
	private void initPipes() {
		spawnPipe(pipeStart);
		spawnPipe(2*pipeStart);
		spawnPipe(3*pipeStart);
	}
	
	private void spawnPipe(float x) {
		pipes.add(PipeFactory.getPype(x));
	}
	
	private void clearScreen() {
		image(backgroundImage, 0, 0);
	}
	
	private void drawGenerationInfo() {
		textSize(32);
		fill(255);
		score = agent.getBestScore();
		highscore = score > highscore ? score : highscore;
		text("Score: " + score, 20, 50);
		text("Generation: " + agent.generation, 20, 100);
		text("Alive: " + agent.alive + " / " + agent.populationSize, 20, 150);
		text("Highscore: " + highscore, 20, 200);
		text("Speed: " + speed, 20, 250);
	}
	
	private void renderNeuralNetwork() {
		NeuralNetwork net = this.agent.getBestGenome();
		int beginx = 1100;
		int beginy = 20;
		int yspan = 300;
		int layerSpace = 80;
		int neuronSpace = 10;
		int layerWidth = 25;
		ellipseMode(CENTER);
		for (int i = 1; i < net.layers.size(); i++) {
			Layer prevLayer = net.layers.get(i-1);
			Layer layer = net.layers.get(i);
			int totalLayerHeight = layer.neurons.size()*layerWidth + (layer.neurons.size()-1)*neuronSpace;
			int layerBegin = beginy + (yspan - totalLayerHeight)/2;
			int totalPrevLayerHeight = prevLayer.neurons.size()*layerWidth + (prevLayer.neurons.size()-1)*neuronSpace;
			int prevLayerBegin = beginy + (yspan - totalPrevLayerHeight)/2;
			// Draw current layer
			for (int j = 0; j < layer.neurons.size(); j++) {
				// Draw weights for each neuron
				Neuron neuron = layer.neurons.get(j);
				for (int k = 0; k < neuron.weights.size(); k++) {
					float weight = neuron.weights.get(k);
					strokeWeight(2*Math.abs(weight));
					if (weight >= 0) {
						stroke(0, 0, 255);
					} else {
						stroke(255, 0, 0);
					}
					line(beginx + (i-1)*(layerWidth+layerSpace), prevLayerBegin + k*(layerWidth + neuronSpace), beginx + i*(layerWidth+layerSpace), layerBegin + j*(neuronSpace + layerWidth));
				}
				fill(255);
				strokeWeight(1);
				stroke(0);
				ellipse(beginx + i*(layerWidth+layerSpace), layerBegin + j*(neuronSpace + layerWidth), layerWidth, layerWidth);
			}
			// Draw previous layer
			for (int j = 0; j < prevLayer.neurons.size(); j++) {
				fill(255);
				strokeWeight(1);
				stroke(0);
				ellipse(beginx + (i-1)*(layerWidth+layerSpace), prevLayerBegin + j*(neuronSpace + layerWidth), layerWidth, layerWidth);
			}
		}
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
