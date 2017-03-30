package com.mygdx.gdx1;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Activity1 extends Game{
	Stage stage;
	Skin skin;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);
		Gdx.app.log("GDX1 : ", "Activity starting");
		// For android back
		Gdx.input.setCatchBackKey(true);
		this.screen = new MenuScreen(this);
		this.screen.show();
	}

	@Override
	public void render () {
		super.render();
		boolean bol = Gdx.input.isKeyPressed(Input.Keys.ESCAPE);
		bol |= Gdx.input.isKeyPressed(Input.Keys.BACK);
		if (bol){
			setScreen(new MenuScreen(this));
		}
	}

	
	@Override
	public void setScreen (Screen screen) {
		Gdx.input.setInputProcessor(null);
		super.setScreen(screen);
	}


}
