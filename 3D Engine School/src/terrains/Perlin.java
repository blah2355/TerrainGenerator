package terrains;

import java.util.Random;

public class Perlin {
	
	private static final float AMPLITUDE = 70f;
	private static long ops;
	
	public Perlin(){
		ops = 0;
	}

	public long getOps(){
		return ops;
	}
	
	private static float[][] generateWhiteNoise(int width, int height) {
		Random rand = new Random(0);
		int seed = rand.nextInt(1000000000);
		float noise[][] = new float[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				rand.setSeed(i * 49632 + j * 325176 + seed);
				noise[i][j] = (rand.nextFloat() * AMPLITUDE) - (AMPLITUDE / 2);
				ops++;
			}
		}

		return noise;
	}

	private static float interpolate(float x0, float x1, float alpha) {
		ops++;
		return x0 * (1 - alpha) + alpha * x1;
	}

	private static float[][] generateSmoothNoise(float[][] base, int octave) {
		int width = base.length;
		int height = base[0].length;

		float[][] smoothNoise = new float[width][height];

		int sampleT = 1 << octave;
		float sampleF = 1f / sampleT;

		for (int i = 0; i < width; i++) {
			int sample_i0 = (i / sampleT) * sampleT;
			ops++;
			int sample_i1 = (sample_i0 + sampleT) % width;
			ops++;
			float horizontal_blend = (i - sample_i0) * sampleF;
			ops++;

			for (int j = 0; j < height; j++) {
				int sample_j0 = (j / sampleT) * sampleT;
				ops++;
				int sample_j1 = (sample_j0 + sampleT) % height;
				ops++;
				float vertical_blend = (j - sample_j0) * sampleF;
				ops++;

				float top = interpolate(base[sample_i0][sample_j0], base[sample_i1][sample_j0], horizontal_blend);

				float bottom = interpolate(base[sample_i0][sample_j1], base[sample_i1][sample_j1], horizontal_blend);

				smoothNoise[i][j] = interpolate(top, bottom, vertical_blend);

			}
		}
		return smoothNoise;
	}

	private static float[][] generatePerlinNoise(float[][] base, int octaveCount) {

		int width = base.length;
		int height = base[0].length;

		float[][][] smoothNoise = new float[octaveCount][][]; // an array of 2D
																// arrays
																// containing

		float persistance = 0.5f;

		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = generateSmoothNoise(base, i);
		}

		float[][] perlinNoise = new float[width][height];
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;
		
		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			ops++;
			totalAmplitude += amplitude;
			ops++;

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
					ops++;
				}
			}
		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				perlinNoise[i][j] /= totalAmplitude;
				ops++;
			}
		}

		return perlinNoise;
	}
	
	public float[][] createGrid(int vertexCount){
		return generatePerlinNoise(generateWhiteNoise(vertexCount, vertexCount), 6);
	}
}
