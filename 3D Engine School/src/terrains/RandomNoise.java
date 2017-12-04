package terrains;

import java.util.Random;

public class RandomNoise {
	
	private static int VERTEX_COUNT; // Default 128 x 128
	
	private static float[][] heights = new float[128][128];
	
	private static float [][] gen_heights = new float[132][132];
	
	private static long ops;
	
	private static final int AMPLITUDE = 150;
	
	private static Random r = new Random();
	
	private static void generateHeights() {
		
		for (int i = 0; i < VERTEX_COUNT + 4; i++) {
			for (int j = 0; j < VERTEX_COUNT + 4; j++) {
				gen_heights[i][j] = r.nextInt(AMPLITUDE) - AMPLITUDE/2;
				ops++;
			}
		}
	}
	
	public RandomNoise(){
		ops = 0;
	}
	
	public long getOps(){
		return ops;
	}
	  

    private static float getSmoothNoise(int j, int i) {
		float corners = (gen_heights[j-1][i-1] + gen_heights[j+1][i-1] 
				+ gen_heights[j-1][i+1] + gen_heights[j+1][i+1]) / 16f;
		float sides = (gen_heights[j-1][i] + gen_heights[j+1][i] 
				+ gen_heights[j][i-1] + gen_heights[j][i+1])/8f;
		float center = gen_heights[j][i]/4f;
    	ops++;
		return corners+sides+center;
    	
    }
   
    
	public float[][] createGrid(int vertexCount) {
		VERTEX_COUNT = vertexCount;
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		generateHeights();
		
		for (int runs = 1; runs <= 3; runs++) {
			for (int i = 1; i < gen_heights.length - 1; i++) {
				for (int j = 1; j < gen_heights[0].length - 1; j++) {
					gen_heights[j][i] = getSmoothNoise(j, i);
				}
			}
		}
		
		for (int i = 2; i < gen_heights.length -2; i++) {
			for (int j = 2; j < gen_heights[0].length - 2; j++) {
				heights[j-2][i-2] = gen_heights[j][i];
			}
		}
		
		
		return heights;
	}

}
