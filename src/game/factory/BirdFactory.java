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

package game.factory;

import game.model.Bird;
import neuroevolution.neuralnetwork.NeuralNetwork;
import util.Screen;

public class BirdFactory {

	private BirdFactory() {
		
	}
	
	public static Bird getBird() {
		return new Bird(Screen.WIDTH/5.0f, Screen.HEIGHT/2.0f, 20);
	}
	
	public static Bird getBird(NeuralNetwork.FlattenNetwork net) {
		Bird bird = getBird();
		bird.net.expand(net);
		return bird;
	}
	
	public static float getSpawnX() {
		return Screen.WIDTH/5.0f;
	}
	
	public static float getRadius() {
		return 20f;
	}
}
