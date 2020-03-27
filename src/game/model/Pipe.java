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

import util.Screen;

public class Pipe {
	
	public float height;
	public float x;
	public float velocity = -3f;
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
