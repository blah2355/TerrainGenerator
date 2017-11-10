package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths {

	// Shortcut methods for creating matrices
	
	// Creates a transformation matrix that rotates the matrix by x, y, and z values defined elsewhere. 
	// This transformation can also translate a matrix by a vector or scale the values in the matrix
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;	
	}
	
	// Basically the same as the above methods, leaving out the scale.
	// Rotates the matrix based on the cameras yaw, roll and pitch, and translates the matrix to be "facing" the opposite direction of the camera, giving the illusion of a camera
	public static Matrix4f createViewMatrix(Camera camera){
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate(((float) Math.toRadians(camera.getPitch())), new Vector3f(1, 0, 0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate(((float) Math.toRadians(camera.getYaw())), new Vector3f(0, 1, 0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate(((float) Math.toRadians(camera.getRoll())), new Vector3f(0, 0, 1), viewMatrix,
				viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, - cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	
}
