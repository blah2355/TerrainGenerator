package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import entities.Camera;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import terrains.Terrain;

public class MainGameLoop {

	public static void main(String[] args) {

		// Initializations
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Terrain terrain = new Terrain(0, -1, loader);
		Camera camera = new Camera(terrain.getSize() / 2, terrain.getSize() / 2);
		Renderer renderer = new Renderer(loader, camera);

		// While the program is not closed, perform main calculations

		while (!Display.isCloseRequested()) {

			// Moves the camera
			camera.move();
			// Renders the terrain using the cameras POV
			renderer.render(terrain, camera);
			// Checks for inputs to change terrain
			checkInputs(terrain);

			DisplayManager.updateDisplay();
		}

		// Cleans up the memory before closing the display
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	
	private static void checkInputs(Terrain terrain) {
		// Uses basic LWJGL keyboard commands to control the program
		while (Keyboard.next()) { // While the keyboard has an event (button being pressed)
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) { // If the event detected is escape
				DisplayManager.closeDisplay();	// close the program and exit
				System.exit(1);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_1) { // If the event is at 1
				if (Keyboard.getEventKeyState()) { // Makes sure that this event is a new event (prevents from holding down a key and rapidly generating terrains)
					terrain.setGenerator(0); // Change the noise function the generator uses
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_2) { // Same as above but for key 2
				if (Keyboard.getEventKeyState()) {
					terrain.setGenerator(1);
				}
			}
		}

	}

}