package util;

public class Screen {
	
	public static int WIDTH;
	public static int HEIGHT;
	
	private Screen() {
		
	}
	
	public static void setDimensions(int w, int h) {
		WIDTH = w;
		HEIGHT = h;
	}
	
}
