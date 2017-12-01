package terrains;

import java.util.Random;

public class DiamondSquare {
	
	/* This algorithm follows the basic description:
	 * 		Set the 4 corners of the data to some initial value
	 * 		Create a square from 4 points
	 * 		Set the midpoint to the average of the 4 corners, plus a random offset
	 * 		Create a diamond (tilted square) from the 2 points on the squares and 2 midpoints
	 * 		Set the midpoint of the diamond to the average of the 4 corners, plus a random offset
	 * 		Reduce the side length of the shapes by 2
	 * 		Repeat until all points have a value
	 */
	private static long ops;
	
	private static int VERTEX_COUNT;
	// an initial seed value for the corners of the data
	final static float SEED = 100000f;

	private static float[][] heights;
	
	public DiamondSquare(){
		ops = 0;
	}
	
	public long getOps(){
		return ops;
	}

	private static void generateHeights() {
		// seed the 4 corners
		heights[0][0] = SEED;
		heights[0][VERTEX_COUNT - 1] = SEED;
		heights[VERTEX_COUNT - 1][0] = SEED;
		heights[VERTEX_COUNT - 1][VERTEX_COUNT - 1] = SEED;

		Random r = new Random();

		float offset = SEED;// the range (-offset -> +offset) for the average offset

		for (int sideLength = VERTEX_COUNT - 1; sideLength >= 2; sideLength /= 2, offset /= 2.0) {
			// for creating each square, the size of each side of the square must be at least 2, 
			// otherwise the calculation cannot find a halfway point to base the center point off of
			
			int half = sideLength / 2;
			ops++;
			// Iterate through the array of heights by using the side length of each square
			// x and y represent the "top left" of the square created
			// x + sideLength and y + sideLength represent the "bottom right"
			for (int x = 0; x < VERTEX_COUNT - 1; x += sideLength) {
				for (int y = 0; y < VERTEX_COUNT - 1; y += sideLength) {
					
					
					float average = heights[x][y] + 					// top left
							heights[x + sideLength][y] + 				// top right
							heights[x][y + sideLength] + 				// bottom left
							heights[x + sideLength][y + sideLength];	// bottom right
					average /= 4.0;
					ops++;
					// x + half, y + half represents the center of the square
					// The center is set to the average of the points plus a random offset
					// that has a value on [-offset, offset]
					// The offset value decreases, ensuring that with each iteration the value
					// of the height does not get too large.
					heights[x + half][y + half] = average + (r.nextFloat() * 2 * offset) - offset;
					ops++;
				}
			}

			
			// creates diamonds and sets their center point to the average
			// only necessary to change x by sideLength/2 because diamonds are not in the same row
			
			// x and y < VERTEX_COUNT -1 in order to allow wrap around to the other side of the array
			for (int x = 0; x < VERTEX_COUNT - 1; x += half) {
				// and y = x offset by half a side, but moved by
				// the full side length. Diamonds in the y direction are offset by a full side length
				for (int y = (x + half) % sideLength; y < VERTEX_COUNT - 1; y += sideLength) {
					// y changes by the full side length because diamonds are in the same column
					// y starts at an offset of x because diamonds can't start where squares do
					
					// x, y is center of diamond
					// A modulator and adding VERTEX_COUNT for subtraction has to be used to allow the data to "wrap"
					// Without this, the side values do not truly represent a diamond square algorithms results
					
					// Totals the corners
					float avg = heights[(x - half + VERTEX_COUNT - 1) % (VERTEX_COUNT - 1)][y] + // Left
					heights[(x + half) % (VERTEX_COUNT - 1)][y] + // Right
					heights[x][(y - half + VERTEX_COUNT - 1) % (VERTEX_COUNT - 1)] + // Top
					heights[x][(y + half) % (VERTEX_COUNT - 1)]; // Bottom
					
					// Averages of the corners
					avg /= 4.0;
					ops++;

					// Adds a random offset value.
					// nextFloat() returns a float on [0,1.0] so this scales it to be on [-offset, offset]
					avg = (avg + (r.nextFloat() * 2 * offset) - offset);
					// update value for center of diamond
					heights[x][y] = avg;
					ops++;

					// Sets the values of 2 edges to the average 
					if (x == 0)
						heights[VERTEX_COUNT - 1][y] = avg;
					if (y == 0)
						heights[x][VERTEX_COUNT - 1] = avg;
				}
			}
		}
	}

	public float[][] createGrid(int vertexCount) {
		VERTEX_COUNT = vertexCount + 1; // This algorithm requires maps of size 2^n + 1, so 1 is added since all maps are of size 128 (2^7)
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		generateHeights();
		for (int i = 0; i < heights.length; i++) {
			for (int j = 0; j < heights[0].length; j++) {
				heights[j][i] = (heights[j][i] / (SEED / 50)) - (SEED / 1000 / 2);
			}
		}
		return heights;
	}
}