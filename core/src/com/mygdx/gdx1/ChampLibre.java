package com.mygdx.gdx1;

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
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class ChampLibre implements Screen{
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
        cam.position.set(200f, 200f, 200f);
        cam.lookAt(0,0,0);
        cam.near = 10f;
        cam.far = 50000f;
        cam.update();

		camController = new CameraInputController(cam);
		camController.scrollFactor = -100;
	    Gdx.input.setInputProcessor(camController);

        assets = new AssetManager();
        assets.load("model/champ/champ_libre.g3db", Model.class);
        loading = true;
	}

		
    private void doneLoading() {
        Model ship = assets.get("model/champ/champ_libre.g3db", Model.class);
        instance = new ModelInstance(ship);
        instance.transform.setToTranslation(0, 0, 0);
		loading = false;
    }


    @Override
    public void render (float delta) {
		if (loading && assets.update()){
			doneLoading();
		}
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.app.log("Full city 1 ", "" + instance);
		if (null != instance){
			camController.update();
			modelBatch.begin(cam);
			modelBatch.render(instance, environment);
			modelBatch.end();
		}
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
