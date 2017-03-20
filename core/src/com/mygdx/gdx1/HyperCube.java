package com.mygdx.gdx1;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class HyperCube implements Screen {
	// Standard 
	private PerspectiveCamera cam;
	private CameraInputController inputController;
	private ModelInstance instance;
	private Environment environment;

	// Disposable
	private ModelBatch modelBatch;
	private Model model;

	// USED
    private double vertices[][];
	private byte edges[][];
	// faces1 : w=-1 | 2 : w=1 | 3 : w ot constant
	private ArrayList<int[]> faces1 = new ArrayList<int[]>();
	private ArrayList<int[]> faces2 = new ArrayList<int[]>();
	private ArrayList<int[]> faces3 = new ArrayList<int[]>();
	// Matrix
    private double[][] ROT4 = MathUtil.id;	
	private double[][] vel = MathUtil.id;

	// Param
    double getSpeed() { return  10;}
	Color faceColor1 = new Color(0, 0, 1, 0.20f);
	Color faceColor2 = new Color(1, 0, 0, 0.20f);
	Color faceColor3 = new Color(1, 1, 0, 0.05f);
	Color colorEdge = new Color(1, 1, 0, 1);



	@Override
	public void show() {
		// CTX 
		modelBatch = new ModelBatch(new DefaultShaderProvider());
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		// CAM 
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 30f;
		cam.update();

		// Model
		initHyperCube();
		modelHyperCube();

		// Input 
		Gdx.input.setInputProcessor(inputController = new CameraInputController(cam));
	}

	@Override
	public void render(float delta) {
		// Update
		inputController.update();
		vel = MathUtil.updateVelocityMatrix(vel);
		ROT4 = MathUtil.mulMatrix(ROT4, vel);
		modelHyperCube();

		// Render
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		model.dispose();
	}

	@Override
	public void hide(){}

	@Override
	public void pause(){}

	@Override
	public void resize(int arg0, int arg1) {}

	@Override
	public void resume(){}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Utils 
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void modelHyperCube(){
		// Begin batch
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();

		// Lines 
		MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
		builder.setColor(colorEdge);
		for (byte[] edge : edges){
			double p1[] = MathUtil.rotateVertex(ROT4, vertices[edge[0]]);
			double p2[] = MathUtil.rotateVertex(ROT4, vertices[edge[1]]);
			builder.line((float) p1[0], (float) p1[1],(float) p1[2],
					(float) p2[0],(float) p2[1],(float) p2[2]);
		}

		// Faces
		drawFaces(modelBuilder, faces1, faceColor1);
		drawFaces(modelBuilder, faces2, faceColor2);
		drawFaces(modelBuilder, faces3, faceColor3);

		// End batch
		model = modelBuilder.end();
		instance = new ModelInstance(model);
	}

	public void drawFaces(ModelBuilder modelBuilder, ArrayList<int[]> faces, Color color){
		Material faceMaterial = new Material();
		faceMaterial.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));	
		MeshPartBuilder builder2 = modelBuilder.part("face", GL20.GL_TRIANGLES, 3, faceMaterial);
		builder2.setColor(color);
		for (int[] face : faces){
			double p1[] = MathUtil.rotateVertex(ROT4, vertices[face[0]]);
			double p2[] = MathUtil.rotateVertex(ROT4, vertices[face[1]]);
			double p3[] = MathUtil.rotateVertex(ROT4, vertices[face[2]]);
			double p4[] = MathUtil.rotateVertex(ROT4, vertices[face[3]]);
			// 1, 2, 3 // 
			builder2.triangle(
					new Vector3((float) p1[0],(float) p1[1],(float) p1[2]),
					new Vector3((float) p2[0],(float) p2[1],(float) p2[2]),
					new Vector3((float) p3[0],(float) p3[1],(float) p3[2])
					);
			// 1, 2, 4
			builder2.triangle(
					new Vector3((float) p1[0],(float) p1[1],(float) p1[2]),
					new Vector3((float) p2[0],(float) p2[1],(float) p2[2]),
					new Vector3((float) p4[0],(float) p4[1],(float) p4[2])
					);
			// 3, 4, 1
			builder2.triangle(
					new Vector3((float) p3[0],(float) p3[1],(float) p3[2]),
					new Vector3((float) p4[0],(float) p4[1],(float) p4[2]),
					new Vector3((float) p1[0],(float) p1[1],(float) p1[2])
					);
			// 3, 4, 2
			builder2.triangle(
					new Vector3((float) p3[0],(float) p3[1],(float) p3[2]),
					new Vector3((float) p4[0],(float) p4[1],(float) p4[2]),
					new Vector3((float) p2[0],(float) p2[1],(float) p2[2])
					);
		}
	}

	public void initHyperCube(){
        // Create the vertices [1, 1, 1, -1]
        vertices = new double[16][4];
        for (int i=0; i < 16; i++) {
            for (int j=0; j < 4; j++){
				vertices[i][j] = (((i >> (3-j)) & 1) - 0.5) * 2;
			}
        }

        // Create the edges: 3 coords in common 
        edges = new byte[32][2];
        int k = 0;
        for (int i=0; i < 15; i++) {
            for (int j=i+1; j < 16; j++) {
                int ct = 0;
                for (int dif=i^j; dif != 0; dif >>= 1) if ((dif&1) != 0) ct++;
                if (ct == 1) {
                    edges[k][0] = (byte)i;
                    edges[k][1] = (byte)j;
                    k++;
                }
            }
        }

		// Create the faces : 2 coords in common 
		for (int m=0; m < 4; m++) {
        for (int n=0; n < m; n++) {
		for (int bit1 = -1; bit1 <= 1; bit1 +=2){
		for (int bit2 = -1; bit2 <= 1; bit2 +=2){
			int plane_index = 0;
			int v_array[] = new int[4];
			for (int v= 0; v < vertices.length; v++){
				boolean bol = (vertices[v][m] * bit1 > 0);
				bol &= (vertices[v][n] * bit2 > 0);
				if (bol){
					v_array[plane_index] = v;
					plane_index++;
				}
			}
			if (m != 3){
				faces3.add(v_array);
			}
			else{
				if (bit1 > 0){
					faces1.add(v_array);
				}
				else{
					faces2.add(v_array);
				}
			}
        }}}}
	}



	public static class MathUtil{
		private static double e = 0.000005f;
		private static double e1 = Math.sin(e);
		private static double e2 = Math.cos(e);
    	private static Random rand = new Random();

    	private static double id[][] = {	
			{1, 0, 0, 0},
			{0, 1, 0, 0},
			{0, 0, 1, 0},
			{0, 0, 0, 1}
		};
    	private static double mid[][] = {	
			{-1, 0, 0, 0},
			{0, -1, 0, 0},
			{0, 0, -1, 0},
			{0, 0, 0, -1}
		};
		private static double rotX[][] = {
			{     e2,    -e1,  	   0,  	   0},
			{     e1,     e2,      0,  	   0},
			{      0,      0,      1,  	   0},
			{  	   0,      0,	   0,  	   1},
		};
		private static double rotY[][] = {
			{      1,      0,  	   0,  	   0},
			{      0,     e2,    -e1,  	   0},
			{      0,     e1,     e2,  	   0},
			{  	   0,      0,	   0,  	   1},
		};
		private static double rotZ[][] = {
			{      1,      0,  	   0,  	   0},
			{      0,      1,      0,  	   0},
			{      0,      0,     e2,  	 -e1},
			{  	   0,      0,	  e1,  	  e2},
		};
		private static double rotW[][] = {
			{     e2,      0,  	   0,  	 -e1},
			{      0,      1,      0,  	   0},
			{      0,      0,      1,  	   0},
			{  	  e1,      0,	   0,  	  e2},
		};

		// res <- M1 x M2
		public static double[][] mulMatrix(double[][] matrix1, double[][] matrix2){
			double[][] res = new double[4][4];
			for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				for (int k = 0; k < 4; k++){
					res[i][j] += matrix1[i][k] * matrix2[k][j];
				}
			}}
			return res;
		}

		// res <- ROT4.vertex
		public static double[] rotateVertex(double[][] ROT4, double vertex[]){
			double res[] = new double[4];
			for (int j=0; j < 4; j++) {
				for (int k=0; k < 4; k++){
					res[j] += (ROT4[j][k] * vertex[k]);
				}
			}
			return res;
		}

		public static double[][] randMulMatrix(double[][] vel, double[][] base){
			double[][] res;
			if (rand.nextInt(2) == 0){
				res = id.clone();
			}
			else{
				res = mid.clone();
			}
			int iRand = rand.nextInt(5);
			for (int i=0; i < iRand; i++){
				res = mulMatrix(res, base);
			}
			res = mulMatrix(vel, res);
			return res;
		}


		public static double[][] updateVelocityMatrix(double[][] velIn){
			double[][] vel = new double[4][4];
			vel = randMulMatrix(velIn, rotX);
			vel = randMulMatrix(vel, rotY);
			vel = randMulMatrix(vel, rotZ);
			vel = randMulMatrix(vel, rotW);
			return vel;
		}
	}
}
