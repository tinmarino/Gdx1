package com.mygdx.gdx1;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/*
 	Speed from Gps, neeed AndroidGpsSpeed to give me it's position
	Do I need an inteface ?
*/

public class GpsSpeed implements Screen {
	Stage stage;
	SkinLib skinLib;
	TextButton textButton;
	Label label;
	Table table;
	GpsInterface gpsInterface;

	@Override
	public void show(){
		stage = new Stage();
		skinLib = new SkinLib();
		table = new Table();
		if (Gdx.app.getType() == ApplicationType.Android){
			gpsInterface = GpsInterface.Container.Instance;
		}
		
		// Speed
		textButton = new TextButton("0", skinLib.getTextButton());

		// Speed
		label = new Label("0\n0\n0", skinLib.getLabel());


		table.add(textButton).fill().expand().row();
		table.add(label).fill().expandX();
		table.setFillParent(true);
		stage.addActor(table);
	}


	public void update(){
		if (gpsInterface != null){
			textButton.setText("" + (int) (3.6 * gpsInterface.getGPSSpeed()));
			String text = "Atitutude: " + (int) gpsInterface.getGPSAltitude() + " m\n";
			text += "Latitude: " + gpsInterface.getGPSLatitude() + "\n";
			text += "Longitude: " + gpsInterface.getGPSLongitude() + "\n";
			label.setText(text);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resume() { }

	@Override
	public void dispose(){
		skinLib.dispose();
		stage.dispose();
	}

	@Override
	public void hide() { }

	@Override
	public void pause() { }

	@Override
	public void resize(int arg0, int arg1) { }
}
