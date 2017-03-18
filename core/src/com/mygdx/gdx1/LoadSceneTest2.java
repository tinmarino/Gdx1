
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class LoadSceneTest2 implements Screen{
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading;

    public ModelInstance ship;
    public ModelInstance space;

    @Override
    public void show() {
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 70f, 100f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300000f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);


        assets = new AssetManager();
        assets.load("data/scene.g3dj", Model.class);
        loading = true;
    }

    private void doneLoading() {
		float pixel2world = 0.01f;
    	Model model = assets.get("data/scene.g3dj", Model.class);
        ship = new ModelInstance(model, "ship");
		ship.transform.scale(pixel2world, pixel2world, pixel2world);
        ship.transform.setToRotation(Vector3.Y, 180).trn(0, 0, 6f);
		ship.calculateTransforms();
        instances.add(ship);

        for (float x = -5f; x <= 5f; x += 2f) {
            ModelInstance block = new ModelInstance(model, "block");
			block.transform.scale(pixel2world, pixel2world, pixel2world);
            block.transform.setToTranslation(x, 0, 3);
            instances.add(block);
        }

        for (float x = -5f; x <= 5f; x += 2f) {
            for (float z = -8f; z <= 0f; z += 2f) {
                ModelInstance invader = new ModelInstance(model, "invader");
                invader.transform.setToTranslation(x * pixel2world, 0, z * pixel2world);
                //instances.add(invader);
            }
        }

        space = new ModelInstance(model, "space");
		//space.transform.scale(pixel2world, pixel2world, pixel2world);

        loading = false;
    }

    @Override
    public void render(float delta) {
        if (loading && assets.update())
            doneLoading();
        camController.update();
		Gdx.app.log("LoadSceneTest2 :", "camera is at" + cam.position + "NULL : " + (null == space));

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        if (space != null)
            modelBatch.render(space);
        modelBatch.end();
    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    @Override
    public void resume () {
    }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void pause () {
	}

	@Override
	public void hide() {
	}
}
