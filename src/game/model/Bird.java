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

package game.model;

import neuroevolution.neuralnetwork.NeuralNetwork;
import util.Screen;

public class Bird {
	public float x;
	public float y;
	public float radius;
	public float velocity;
	public float gravity;
	public float airDrag;
	public float flapForce;
	public boolean isDead = false;
	public int score;
	
	public NeuralNetwork net;
	
	private Bird() {
		this.velocity = -8f;
		this.gravity = 20;
		this.airDrag = 0.9f;
		this.flapForce = -26f;
		this.isDead = false;
		this.score = 0;
	}
	
	public Bird(float x, float y, float r) {    
		this();
		this.x = x;
		this.y = y;
		this.radius = r;
		this.net = new NeuralNetwork(3, 8, 2);
	}
	
	public void update() {
		this.score++;
		this.velocity *= airDrag;
		this.velocity += gravity;
		this.y += velocity;
	}
	
	public void feed(Pipe closestPipe, float distance) {
		if (closestPipe != null && !this.isDead) {
			float[] inputs = {
				distance / Screen.WIDTH,
				(this.y + this.radius - (Screen.HEIGHT - closestPipe.height)) / Screen.HEIGHT,
				((Screen.HEIGHT - closestPipe.gap - closestPipe.height) - this.y - this.radius) / Screen.HEIGHT,
			};
			float[] output = this.net.eval(inputs);
			if (output[0] > output[1])
				this.flap();
		}
	}
	
	public void flap() {
		this.velocity += this.flapForce;
	}
}
