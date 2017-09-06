package com.mygdx.gdx1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class CardGame implements Screen {
    SpriteBatch spriteBatch;
    TextureAtlas atlas;
    Sprite front;
    Sprite back;

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        atlas = new TextureAtlas("card/carddeck.atlas");

        front = atlas.createSprite("clubs", 2);
        front.setPosition(100, 100);

        back = atlas.createSprite("back", 3);
        back.setPosition(300, 100);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        spriteBatch.begin();
        front.draw(spriteBatch);
        back.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        atlas.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
}
