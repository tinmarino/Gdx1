package com.mygdx.gdx1;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.UBJsonReader;

public class NewYork implements Screen{
	private ModelBatch modelBatch;
	private Environment environment;
	private PerspectiveCamera cam;
	private AssetManager assets;
	ModelInstance instance;
	private CameraInputController camController;
	boolean loading;

	@Override
    public void show() {
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(200f, 200f, 200f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(20f, 20f, 20f);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 50f;
        cam.update();

		camController = new CameraInputController(cam);
		camController.scrollFactor = -1;
	    Gdx.input.setInputProcessor(camController);



		UBJsonReader jsonReader = new UBJsonReader();

		G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
		Model model = modelLoader.loadModel(Gdx.files.getFileHandle("model/new_york.g3db", Files.FileType.Internal));
		instance = new ModelInstance(model);
	}


    @Override
    public void render (float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camController.update();

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();
    }

	@Override
	public void dispose() {
	   	modelBatch.dispose();
        assets.dispose();
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
