package com.mygdx.gdx1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MenuScreen implements Screen {
	Stage stage;
	SkinLib skinLib;
	Game game;


	public MenuScreen(Game game){
		this.game = game;
	}

	@Override
	public void show() {
		stage = new Stage();
		//stage.setViewport(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		Gdx.input.setInputProcessor(stage);
		skinLib = new SkinLib();
		Table table = new Table();

		for (final String i : Tests.getNames()){
			TextButton textButton = new TextButton(i, skinLib.getTextButton());
			textButton.addListener(new ClickListener(){
				@Override
				public void clicked (InputEvent event, float x, float y) {
					Gdx.app.log("GDX1 : ", "click button " + i);
					setScreenWithName(i);
				}
			});
			table.add(textButton).row();
			Gdx.app.log("GDX1 : ", "Adding button " + i);
		}

		ScrollPane scrollPane = new ScrollPane(table);
		scrollPane.setFillParent(true);
		stage.addActor(scrollPane);
	}



	public void setScreenWithName(String stg){
		Gdx.app.log("GDX1 : ", "Setting Screen to " + stg);
		Screen screen = getScreenWithName(stg);
		this.game.setScreen(screen);
	}

	public Screen getScreenWithName(String stg){
		switch (stg){
			case "BabyPony":
				return new BabyPony();
			case "HyperCube":
				return new HyperCube();
			default :
				return new BabyPony();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() { }

	@Override
	public void hide() { }

	@Override
	public void pause() { }

	@Override
	public void resize(int arg0, int arg1) { }

	@Override
	public void resume() { }

}
