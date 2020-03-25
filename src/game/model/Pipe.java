package game.model;

import util.Screen;

public class Pipe {
	
	public float height;
	public float x;
	public float velocity = -5f;
	public float width = 60f;
	
	public final float gap = 180;
	
	private boolean birdHasPassed = false;
	
	public Pipe(float x, float height) {
		this.x = x;
		this.height = height;
	}
	
	public void update() {
		this.x += velocity;
	}
	
	public boolean isInvisible() {
		return this.x < -this.width;
	}
	
	public boolean checkBirdCollision(Bird bird) {
		if (checkBottomPipe(bird))
			return true;
		return checkTopPipe(bird);
	}
	
	public boolean checkPass(Bird bird) {
		if (bird.x - bird.radius > this.x + this.width && !birdHasPassed) {
			birdHasPassed = true;
			return true;
		}
		return false;
	}
	
	private boolean checkTopPipe(Bird bird) {
		return checkBirdRect(bird, this.x, 0, this.width, Screen.HEIGHT - this.gap - this.height);
	}
	
	private boolean checkBottomPipe(Bird bird) {
		return checkBirdRect(bird, this.x, Screen.HEIGHT - this.height, this.width, this.height);
	}
	
	private boolean checkBirdRect(Bird bird, float x, float y, float w, float h) {
		float testx = bird.x;
		float testy = bird.y;
		if (bird.x < x)
			testx = x;
		else if (bird.x > x + w) 
			testx = x + w;
		if (bird.y < y)
			testy = y;
		else if (bird.y > y + h)
			testy = y + h;
		float distx = bird.x - testx;
		float disty = bird.y - testy;
		return (distx*distx + disty*disty) <= bird.radius*bird.radius;
	}
}
