package com.mygdx.gdx1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class HyperCube implements Screen {
	public PerspectiveCamera cam;
	public CameraInputController inputController;
	public ModelInstance instance;
	public Environment environment;
	// Disposable
	public ModelBatch modelBatch;
	public Model model;

	// CUbe info
	private byte edges[][];       // "from" and "to" vertex indices
    private double vertices[][];  // vertex coords in 4-space

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
		modelHyperCube();

		// Input 
		Gdx.input.setInputProcessor(inputController = new CameraInputController(cam));
	}

	@Override
	public void render(float delta) {
		inputController.update();

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
		// Export to 3D 
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
		builder.setColor(Color.RED);
		for (byte[] edge : edges){
			double p1[] = vertices[edge[0]];
			double p2[] = vertices[edge[1]];
			builder.line((float) p1[0], (float) p1[1],(float) p1[2],
					(float) p2[0],(float) p2[1],(float) p2[3]);
		}
		model = modelBuilder.end();
		instance = new ModelInstance(model);
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
	}

	public void rotate(){
	}


	public void modelCube(){
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
			| Usage.Normal);
		instance = new ModelInstance(model);
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
