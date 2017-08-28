package com.mygdx.gdx1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Align;

/*
 	Basic compass tool for android
	WARNING: Not tested cause I don't have any compass on my phones
	https://github.com/libgdx/libgdx/wiki/Compass
		float azimuth = Gdx.input.getAzimuth();
		float pitch = Gdx.input.getPitch();
		float roll = Gdx.input.getRoll();
*/

public class Compass implements Screen{
	Stage stage;
	SkinLib skinLib;
	boolean isCompassAvailable; 
	Image image = null;

	@Override
	public void show() {
		skinLib = new SkinLib();
		stage = new Stage();

		isCompassAvailable = Gdx.input.isPeripheralAvailable(Peripheral.Compass);
		
		if (isCompassAvailable){
			compassAvailable();
		}
		else{
			compassNotAvailable();
		}
	}

	public void compassAvailable(){
		Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);
		pixmap = PixmapFactory.arrow(pixmap, Color.BLACK);
		image = new Image(PixmapFactory.drawableFromPixmap(pixmap));
		image.setSize(stage.getWidth(), stage.getHeight());
		image.setOrigin(image.getWidth()/2, image.getHeight()/2);
		stage.addActor(image);
	}

	public void compassNotAvailable(){
		TextArea textArea = new TextArea("Compass\rNot\rAvailable", skinLib.getTextField());
		textArea.setAlignment(Align.center);
		textArea.setSize(stage.getWidth(), stage.getHeight());
		stage.addActor(textArea);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Update compass
		if (isCompassAvailable){
			float azimuth = Gdx.input.getAzimuth();
			Gdx.app.log("Gdx1", "azimuth is " + azimuth);
			image.setRotation(azimuth);
		}

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
		skinLib.dispose();
	}

	@Override
	public void hide() { }

	@Override
	public void pause() { }

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void resume() { }

}
