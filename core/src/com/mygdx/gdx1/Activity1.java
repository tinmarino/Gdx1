package com.mygdx.gdx1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Activity1 extends Game{
	Stage stage;
	Skin skin;


	
	@Override
	public void create () {
		this.screen = new MenuScreen(this);
		this.screen.show();
	}


}
