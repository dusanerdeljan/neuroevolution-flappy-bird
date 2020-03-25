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
				(distance + closestPipe.width / 2) / Screen.WIDTH,
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
