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
	private PerspectiveCamera cam;
	private CameraInputController inputController;
	private ModelInstance instance;
	private Environment environment;
    private Random rand = new Random();

	// Disposable
	private ModelBatch modelBatch;
	private Model model;

	// CUbe info
    private double vertices[][];  					// vertex coords in 4-space
	private byte edges[][];       					// "from" and "to" vertex indices
	private ArrayList<int[]> faces = new ArrayList<int[]>(); 						// refernces to 4 vertices index
    private double m1[][] = new double[4][4];		// Used for rot4
    private double m2[][] = new double[4][4];
    private double rot4[][] = new double[4][4];  	// rotation matrix
    private double vel[][] = new double[4][4];  	// Velocity, applyed at each cycle. 
    private double newROT[][] = new double[4][4];	// rot4 saver 
    private double ROT4[][] = {	{1, 0, 0, 0},
								{0, 1, 0, 0},
								{0, 0, 1, 0},
								{0, 0, 0, 1}
							};
    private double holdROT[][];

	// Cube param
	private final double velmax = .03;  			// max velocity, radians per cycle
    private final double velinc = .006; 			// velocity increment, radians
    double getSpeed() { return  0.0002;}



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
		createVertices();
		updateRotationMatrix();
		modelHyperCube();

		// Input 
		Gdx.input.setInputProcessor(inputController = new CameraInputController(cam));
	}

	@Override
	public void render(float delta) {
		// Update
		inputController.update();
		updateRotationMatrix();
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


	public void modelHyperCube(){
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();

		// Lines 
		MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
		builder.setColor(Color.RED);
		for (byte[] edge : edges){
			double p1[] = rotateVertex(vertices[edge[0]]);
			double p2[] = rotateVertex(vertices[edge[1]]);
			builder.line((float) p1[0], (float) p1[1],(float) p1[2],
					(float) p2[0],(float) p2[1],(float) p2[3]);
		}

		// Faces
		Material faceMaterial = new Material();
		faceMaterial.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));	
		MeshPartBuilder builder2 = modelBuilder.part("face", GL20.GL_TRIANGLES, 3, faceMaterial);
		builder2.setColor(new Color(0, 1, 0, 0.01f));
		for (int[] face : faces){
			double p1[] = rotateVertex(vertices[face[0]]);
			double p2[] = rotateVertex(vertices[face[1]]);
			double p3[] = rotateVertex(vertices[face[2]]);
			double p4[] = rotateVertex(vertices[face[3]]);
			// 1, 2, 3
			builder2.triangle(
					new Vector3((float) p1[0],(float) p1[1],(float) p1[2]),
					new Vector3((float) p2[0],(float) p2[1],(float) p2[2]),
					new Vector3((float) p3[0],(float) p3[1],(float) p3[2])
					);
			// 1, 3, 4
			builder2.triangle(
					new Vector3((float) p1[0],(float) p1[1],(float) p1[2]),
					new Vector3((float) p3[0],(float) p3[1],(float) p3[2]),
					new Vector3((float) p4[0],(float) p4[1],(float) p4[2])
					);
		}

		model = modelBuilder.end();
		instance = new ModelInstance(model);
	}



	public double[] rotateVertex(double vertex[]){
 		double res[] = {0, 0, 0, 0};
		for (int j=0; j < 4; j++) {
			for (int k=0; k < 4; k++){
				res[j] += (ROT4[j][k] * vertex[k]);
            }
		}
		return res;
	}


	public void createVertices(){
        int i,j,k,dif,ct;

        vertices = new double[16][4];
        edges = new byte[32][2];

        // create the vertices [1, 1, 1, -1]
        for (i=0; i < 16; i++) {
            for (j=0; j < 4; j++) vertices[i][j] = ((i >> (3-j)) & 1) - 0.5;
        }

        // Create the edges
        // Considering each vertex to be a 4-bit bit-pattern, there
        //   is an edge between each pair of vertices that differ in only
        //   one bit.
        k = 0;
        for (i=0; i < 15; i++) {
            for (j=i+1; j < 16; j++) {
                ct = 0;
                for (dif=i^j; dif != 0; dif >>= 1) if ((dif&1) != 0) ct++;
                if (ct == 1) {
                    edges[k][0] = (byte)i;
                    edges[k][1] = (byte)j;
                    k++;
                }
            }
        }

		// Create the faces
		for (int  ii = 0; ii < 15; ii++){
		for (int  jj = ii + 1; jj < 16; jj++){ 
		for (int  kk = jj + 1; kk < 16; kk++){ 
		for (int  ll = kk + 1; ll < 16; ll++){ 
			if (true){
				int[] tmp = new int[4];
				tmp[0] = ii;
				tmp[1] = jj;
				tmp[2] = kk;
				tmp[3] = ll;
				faces.add(tmp);
			}
		}}}}
		// All possiblility of 4 edges comming back
	}

	public void updateRotationMatrix(){
        double angl,sinangl,cosangl,d,dsq,max,veli,vmax;
        int i,j,k,abi,abj;

        max = 0.0;
        abi = 1;
        abj = 2;

        veli = velinc * getSpeed();
        vmax = velmax * getSpeed();

        // The velocity matrix represents the rotation that is to be performed every cycle.
        // It is a 4x4 antisymmetric matrix, with a determinant of zero.
        // We now change it by a small antisymmetric amount (which generally makes the determinant non-zero).
        for (i=0; i < 3; i++) {
            for (j=i+1; j < 4; j++) {
                d = vel[i][j] + veli * (rand.nextDouble() - 0.5);
                vel[i][j] = d;
                vel[j][i] = -d;
                dsq = d * d;
                if (dsq > max) {      // hang onto the indices of the biggest element
                    max = dsq;
                    abi = i;
                    abj = j;
                }
            }
        }

        if (max < 1.0E-10) return;   // no rotation

        // calculate the square root of the determinant
        d =  vel[0][3] * vel[1][2]
            -vel[0][2] * vel[1][3]
            +vel[0][1] * vel[2][3];

        // We need to adjust so that the determinant is zero
        // (abi,abj) are the indices of the largest element
        // Determine the indices of that element's cofactor:
        switch (abi*10+abj) {
            case 1:  i=2; j=3; break;
            case 2:  i=3; j=1; break;
            case 3:  i=1; j=2; break;
            case 12: i=0; j=3; break;
            case 13: i=2; j=0; break;
            case 23: i=0; j=1; break;
            default: i=0; j=1; break;
        }

        // Adjust the cofactor to make the determinant zero.
        vel[i][j] -= d / vel[abi][abj];
        vel[j][i] = -vel[i][j];

        // Calculate the rotation angle (the sum of the squares of the vel elements)
        angl = 0;
        for (i=0; i < 3; i++) {
            for (j=i+1; j < 4; j++) angl += vel[i][j] * vel[i][j];
        }
        angl = Math.sqrt(angl);
        if (angl < 1.0E-5) return;    // no rotation

        // If the angle is too great, reduce all components of the velocity.
        // (Don't want it to rotate too fast.)
        if (angl > vmax) {
            d = vmax / angl;
            angl = vmax;
            for (i=0; i < 3; i++) {
                for (j=i+1; j < 4; j++) {
                    vel[i][j] *= d;
                    vel[j][i] = -vel[i][j];
                }
            }
        }

        // Now we need to build a rotation matrix from "vel".
        // The rotation matrix can be expressed symbolically as
        // R = lim          (I + (vel/n))^n
        //     (n->infinity)
        //
        // Where I is the identity matrix and  "^n" represents the operation
        //   of multiplying the matrix by itself n times.
        // We expand the exponential as a power series in the matrix "vel", noting that R = exp(vel)
        //   and using the standard power series expansion of the exponential.
        // The "vel" matrix has the property that vel . vel . vel = -angl * angl * vel
        //  (where "." is matrix multiplication)
        // Define a matrix m1 as vel / angl.
        // Then m1 . m1 . m1 = -m1
        // Odd powers of m1 can be written as m1^(2n+1) = (-1)^n * m1
        // Even powers of m1 can be written as m1^(2n+2) = (-1)^n * (m1 . m1)
        // Define m2 as m1 . m1
        // Odd powers of vel can be rewritten:  vel^(2n+1) = angl^(2n+1) * (-1)^n * m1
        // Even powers > 0 of vel can be rewritten: vel^(2n+2) = angl^(2n+2) * (-1)^n * m2
        // Rewrite the power series using m1 and m2.
        // The odd terms are a series expansion of sin(angl) * m1
        // The even terms with n > 0 are a series expansion of (1 - cos(angl)) * m2
        // The zero-order term is the identity matrix.

        // Build m1 by scaling vel by an appropriate factor.
        // m1 has the property that m1 . m1 . m1 = -m1
        // (where the "." is matrix multiplication)
        for (i=0; i < 4; i++) m1[i][i] = 0;
        for (i=0; i < 3; i++) {
            for (j=i+1; j < 4; j++) {
                m1[i][j] = vel[i][j] / angl;
                m1[j][i] = -m1[i][j];
            }
        }

        // Build m2, the square of m1:
        for (i=0; i < 4; i++) {
            for (j=i; j < 4; j++) {
                m2[i][j] = 0.0;
                for (k=0; k < 4; k++) m2[i][j] += (m1[i][k] * m1[k][j]);
                m2[j][i] = m2[i][j];
            }
        }

        // Build the rotation matrix
        cosangl = 1.0 - Math.cos(angl);
        sinangl = Math.sin(angl);
        for (i=0; i < 4; i++) {
            for (j=0; j < 4; j++) rot4[i][j] = sinangl*m1[i][j] + cosangl*m2[i][j];
        }
        for (i=0; i < 4; i++) rot4[i][i] += 1.0;

        // Apply the small rotation "rot4" to the cumulative rotation "ROT4"
        for (i=0; i < 4; i++) {
            for (j=0; j < 4; j++) {
                newROT[i][j] = 0.0;
                for (k=0; k < 4; k++) newROT[i][j] += rot4[i][k] * ROT4[k][j];
            }
        }

        // swap newROT with ROT4
        holdROT = ROT4;
        ROT4 = newROT;
        newROT = holdROT;
	}

	@Override
	public void hide() { }

	@Override
	public void pause() { }

	@Override
	public void resize(int arg0, int arg1) { }

	@Override
	public void resume() { }
}
