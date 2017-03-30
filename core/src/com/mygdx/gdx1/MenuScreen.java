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
import com.badlogic.gdx.utils.Align;

public class MenuScreen implements Screen {
	Stage stage;
	SkinLib skinLib;
	Game game;
	// TMP
	ScrollPane scrollPane;
	Table table;


	public MenuScreen(Game game){
		this.game = game;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skinLib = new SkinLib();
		table = new Table();

		for (final String i : Tests.getNames()){
			TextButton textButton = new TextButton(i, skinLib.getTextButton());
			textButton.getLabel().setAlignment(Align.left);
			textButton.setWidth(Gdx.graphics.getWidth());
			textButton.addListener(new ClickListener(){
				@Override
				public void clicked (InputEvent event, float x, float y) {
					setScreenFromName(i);
				}
			});
			table.add(textButton).fill().left().expand().pad(5).row();
		}


		scrollPane = new ScrollPane(table);
		scrollPane.setFillParent(true);
		stage.addActor(scrollPane);
		stage.setScrollFocus(scrollPane);
	}



	public void setScreenFromName(String stg){
		Gdx.app.log("GDX1 : ", "Setting Screen to " + stg);
		Screen screen = Tests.getScreenFromName(stg);
		this.game.setScreen(screen);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		table.setWidth(Gdx.graphics.getWidth());
		stage.draw();
		Gdx.app.log("GDX1 : ", "Width22 " + scrollPane.getWidth() );
		Gdx.app.log("GDX1 : ", "Widthtrable2 " + table.getWidth() );
	}

	@Override
	public void dispose(){
		skinLib.dispose();
	}

	@Override
	public void hide() { }

	@Override
	public void pause() { }

	@Override
	public void resize(int width, int height) {
		// True to center camera on screen
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void resume() { }

}
