package shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import entities.Camera;
import toolbox.Maths;

public class ShaderProgram {
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_amplitude;
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	// Initializes the shader program then adds the shaders to the program
	public ShaderProgram(){
		vertexShaderID = loadShader("/shaders/vs.txt",GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader("/shaders/fs.txt",GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttribute(0, "position");
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	// Establishes a link between the variables in java and the variables in the shaders
	private void getAllUniformLocations(){
		location_transformationMatrix = GL20.glGetUniformLocation(programID, "transformationMatrix");
		location_projectionMatrix = GL20.glGetUniformLocation(programID, "projectionMatrix");
		location_viewMatrix = GL20.glGetUniformLocation(programID, "viewMatrix");	
		location_amplitude = GL20.glGetUniformLocation(programID, "amplitude");
	}
	
	// Begins the use of the shader program
	public void start(){
		GL20.glUseProgram(programID);
	}
	
	// Ends use of the shader program
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	// Stops use of the shader and removes it from memory
	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	// Binds the attribute of the shader program to a shader variable
	private void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	// Loads a matrix as a uniform variable in the shader
	private void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	// The following three methods use the above method to load the matrices necessary to view the terrain into the shader as uniforms
	public void loadTransformationMatrix(Matrix4f matrix){
		loadMatrix(location_transformationMatrix, matrix);
	}
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
	}
	public void loadProjectionMatrix(Matrix4f projection){
		loadMatrix(location_projectionMatrix, projection);
	}
	public void loadTerrainAmplitude(Vector2f amplitude){
		GL20.glUniform2f(location_amplitude, amplitude.x, amplitude.y);
	}
	
	// Loads the shader line by line for OpenGL to use
	private static int loadShader(String file, int type){
		// Simple text reading algorithm
		StringBuilder shaderSource = new StringBuilder();
		try{
			InputStream in = Class.class.getResourceAsStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Creates and compiles a shader from the Strings of commands read from the file
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
	}
}
