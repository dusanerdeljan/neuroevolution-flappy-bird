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
