package terrains;

import java.util.Random;

public class Wavelet {

	static float[][] tileData;

	static int ARAD = 16;

	static float a;

	static float aCoeffs[] = { 0.000334f, -0.001528f, 0.000410f, 0.003545f, -0.000938f, -0.008233f, 0.002172f, 0.019120f,
			-0.005040f, -0.044412f, 0.011655f, 0.103311f, -0.025936f, -0.243780f, 0.033979f, 0.655340f, 0.655340f,
			0.033979f, -0.243780f, -0.025936f, 0.103311f, 0.011655f, -0.044412f, -0.005040f, 0.019120f, 0.002172f,
			-0.008233f, -0.000938f, 0.003546f, 0.000410f, -0.001528f, 0.000334f };

	;
	private static Random r = new Random();

	private static int mod(int x, int n) {
		int m = x % n;
		return (m < 0) ? m + n : m;
	}

	private static float[][] downSample(float[] from, float[] to, int n, int stride) {

		a = aCoeffs[ARAD];

		for (int i = 0; i < n / 2; i++) {
			to[i * stride] = 0;
			for (int k = 2 * i - ARAD; k <= 2 * i + ARAD; k++) {
				to[i * stride] += aCoeffs[k - 2 * i] * from[mod(k, n) * stride];
			}
		}
		float[][] fromTo = { from, to };
		return fromTo;
	}

	private static float[][] upSample(float[] from, float[] to, int n, int stride) {

		float pCoeffs[] = { 0.25f, 0.75f, 0.75f, 0.25f };

		for (int i = 0; i < n; i++) {
			to[i * stride] = 0;
			for (int k = i / 2; k <= i / 2 + 1; k++) {
				to[i * stride] += pCoeffs[i - 2 * k] * from[mod(k, n / 2) * stride];
			}
		}

		float[][] fromTo = { from, to };
		return fromTo;
	}

	private static void GenerateNoiseTile(int n) {
		if (n % 2 != 0)
			n++;

		int ix, iy, iz, i, sz = n * n * n;

		float[][] temp1 = new float[sz][sz];
		float[][] temp2 = new float[sz][sz];
		float[][] noise = new float[sz][sz];

		for (i = 0; i < n * n * n; i++) {
			noise[i][0] = (r.nextFloat() * 2) - 1;
		}

		for (iy = 0; iy < n; iy++) {
			for (iz = 0; iz < n; iz++) {
				i = iy * n + iz * n * n;
				float[][] fromTo;
				
				fromTo = downSample(noise[i], temp1[i], n, 1);
				noise[i] = fromTo[0];
				temp1[i] = fromTo[1];
				
				fromTo = upSample(temp1[i], temp2[i], n, 1);
				temp1[i] = fromTo[0];
				temp2[i] = fromTo[1];
			}
		}
		
		for (ix = 0; ix < n; ix++) {
			for (iz = 0; iz < n; iz++) {
				i = ix + iz * n * n;
				float[][] fromTo;
				
				fromTo = downSample(temp2[i], temp1[i], n, n);
				noise[i] = fromTo[0];
				temp1[i] = fromTo[1];
				
				fromTo = upSample(temp1[i], temp2[i], n, n);
				temp1[i] = fromTo[0];
				temp2[i] = fromTo[1];
			}
		}
		
		for (ix = 0; ix < n; ix++) {
			for (iy = 0; iy < n; iy++) {
				i = ix + iy * n;
				float[][] fromTo;
				
				fromTo = downSample(temp2[i], temp1[i], n, n*n);
				noise[i] = fromTo[0];
				temp1[i] = fromTo[1];
				
				fromTo = upSample(temp1[i], temp2[i], n, n*n);
				temp1[i] = fromTo[0];
				temp2[i] = fromTo[1];
			}
		}
		
		for(i = 0; i < n*n*n; i++){
			noise[i][i]-=temp2[i][i];
		}
		
		int offset = n/2;
		if(offset %2 == 0 ) offset++;
		for(i = 0, ix = 0; ix < n; ix++){
			for(iy = 0; iy < n; iy++){
				for(iz = 0; iz < n; iz++){
					temp1[i++] = noise[mod(ix + offset, n) + mod(iy + offset, n)* n + mod(iz + offset, n)*n*n];
				}
			}
		}
		for(i = 0; i < n*n*n; i++){
			noise[i][0] += temp1[i][0];
		}
		
		
		for(int j = 0; j <n*n*n; j++){
			for(int k = 0; k < n*n*n; k++){
				tileData[j][k] = noise[j*k][0];
			}
		}

	}

	
	public static float[][] createGrid(int vertexCount){
		GenerateNoiseTile(vertexCount);
		for(int j = 0; j <vertexCount; j++){
			for(int k = 0; k < vertexCount; k++){
				System.out.println(tileData[j][k]);
			}
		}
		return tileData;
	}
}
