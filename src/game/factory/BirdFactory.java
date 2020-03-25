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
