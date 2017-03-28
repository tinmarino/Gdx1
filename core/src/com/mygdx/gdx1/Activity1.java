package com.mygdx.gdx1;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Activity1 extends Game{
	Stage stage;
	Skin skin;


	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);
		Gdx.app.log("GDX1 : ", "Activity starting");
		Tests.init();
		this.screen = new MenuScreen(this);
		this.screen.show();
	}


}
