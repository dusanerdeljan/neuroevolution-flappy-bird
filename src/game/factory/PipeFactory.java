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

import java.util.Random;

import game.model.Pipe;

public class PipeFactory {
	
	private static Random random = new Random();
	private static float minHeight = 100f;
	private static float maxHeight = 500;
	
	private PipeFactory() {
		
	}
	
	public static Pipe getPype(float x) {
		return new Pipe(x, minHeight + random.nextFloat() * (maxHeight - minHeight));
	}
	
}
