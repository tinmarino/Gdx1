package com.mygdx.gdx1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Activity1 extends Game{
	Stage stage;
	Skin skin;


	
	@Override
	public void create () {
		// this.screen = new Basic3DTest();
		// this.screen = new LoadModelTest();
		// this.screen = new LoadSceneTest();
		// this.screen = new LoadSceneTest2();
		// this.screen = new Networking();
		// this.screen = new BehindTheSceneTest();
		// this.screen = new ShaderTest();
		// this.screen = new NetInput();
		// this.screen = new HyperCube();
		// this.screen = new BabyPony();
		// this.screen = new BulletTestCollision();
		this.screen = new MenuScreen(this);
		this.screen.show();
	}


}
