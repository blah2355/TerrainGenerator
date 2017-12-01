package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import charts.BarChart;
import entities.Camera;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import terrains.Terrain;

public class MainGameLoop {

	private static List<Terrain> terrains = new ArrayList<Terrain>();
	private static final int ALGS_COUNT = 5;
	private static final int DATA_FACTOR = 10000;
	private static int t = 0;
	
	public static void main(String[] args) {

		// Initializations of the display and model loader
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		double[] runtimes = new double[ALGS_COUNT];
		long[] ops = new long[ALGS_COUNT];
		
		// Creates terrains and adds them to the terrain list
		Terrain terrain = new Terrain(0, -1, loader);
		terrain.setGenerator(0);
		runtimes[0] = terrain.getRunTime() * DATA_FACTOR;
		ops[0] = terrain.getOps();
		
		Terrain terrain2 = new Terrain(0, 0, loader);
		terrain2.setGenerator(1);
		runtimes[1] = terrain2.getRunTime() * DATA_FACTOR;
		ops[1] = terrain2.getOps();
		
		Terrain terrain3 = new Terrain(1, 0, loader);
		terrain3.setGenerator(2);
		runtimes[2] = terrain3.getRunTime() * DATA_FACTOR;
		ops[2] = terrain3.getOps();
		
		Terrain terrain4 = new Terrain(1, -1, loader);
		terrain4.setGenerator(3);
		runtimes[3] = terrain4.getRunTime() * DATA_FACTOR;
		ops[3] = terrain4.getOps();
		
		Terrain terrain5 = new Terrain(1,-2, loader);
		terrain5.setGenerator(4);
		
		terrains.add(terrain);
		terrains.add(terrain2);
		terrains.add(terrain3);
		terrains.add(terrain4);
		terrains.add(terrain5);
		
		BarChart.createChart(runtimes, ops);
		
		// Initializes camera at the average midpoint of all terrains in the list
		Camera camera = new Camera(averageLocation().x, averageLocation().y);
		//
		Renderer renderer = new Renderer(loader, camera);
		
		
		// While the program is not closed, perform main calculations

		while (!Display.isCloseRequested()) {

			// Moves the camera
			camera.move();
		
			// Renders the terrains using the cameras POV
			renderer.render(terrains, camera);
			
			// Checks for inputs to change terrain
			checkInputs();
			camera.setAnchor(terrains.get(t).getCenter().x, -terrains.get(t).getCenter().y);
			DisplayManager.updateDisplay();
		}

		// Cleans up the memory before closing the display
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	
	}

	
	private static void checkInputs() {
		// Uses basic LWJGL keyboard commands to control the program
		while (Keyboard.next()) { // While the keyboard has an event (button being pressed)
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) { // If the event detected is escape
				DisplayManager.closeDisplay();	// close the program and exit
				System.exit(1);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_1) { // If the event is at 1
				if (Keyboard.getEventKeyState()) { // Makes sure that this event is a new event (prevents from holding down a key and rapidly generating terrains)
					
					terrains.get(0).regenTerrain();
					}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_2) { // Same as above but for key 2
				if (Keyboard.getEventKeyState()) {
					terrains.get(1).regenTerrain();;
				}
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_SPACE){
				if(Keyboard.getEventKeyState()){
					for(Terrain t: terrains){
						t.regenTerrain();
					}
				}
			}
			
			if(Keyboard.getEventKey() == Keyboard.KEY_LEFT){
				if(Keyboard.getEventKeyState()){
					t--;
					if(t < 0){
						t = terrains.size() - 1;
					}
				}
			}
			
			if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT){
				if(Keyboard.getEventKeyState()){
					t++;
					if(t >= terrains.size()){
						t = 0;
					}
				}
			}
			
			
			
			
		}
	}
	
	private static Vector2f averageLocation(){
		// Simple method for setting the camera location to the average of the centers of all drawn terrains
		Vector2f position = new Vector2f();
		for(Terrain t : terrains){
			position.x += t.getX() + (t.getSize() / 2);
			position.y -= t.getZ() + (t.getSize() / 2);
		}
		position.x /= terrains.size();
		position.y /= terrains.size();
		
		return position;
	}

}