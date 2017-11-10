package terrains;

public class NewAlgorithmGuide {
	
	// All algorithms must have these things in order to properly be implemented into the program.
	// I considered just making an abstract class but that was too much work and its a fairly obscure java topic
	
	/*
	 * - A createGrid method with the parameter of the vertex count.
	 * 		- This method must return a 2D array of floats that represent the height of each vertex of the terrain
	 * 		- This method (if necessary) must also scale the height values down to a 1.0 wide interval. This can mean [-.5, .5] or [-1,0], doesn't matter, as long as it is 1.0 wide.
	 * 
	 * - The default input is a 128x128 grid of height values. If a specific algorithm requires more or less, it must be done in the createGrid method
	 * 
	 * - The algorithm class should not have a constructor. It is a static class and the createGrid(vertexCount) method must be called in the form "float[][] heights = thisAlgorithm.createGrid(vertexCount):"
	 * 		This can be seen on line 17 of the Terrain class
	 * 
	 * Eventually I plan on adding an option to select which algorithm should be used while the program is ready, so it is important that they all follow this form in order for them to be demonstrated properly.
	 * 
	 */
}
