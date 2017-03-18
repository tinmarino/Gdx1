package com.mygdx.gdx1;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class BabyPony implements Screen {
	enum WHO{ME, YOU}
	private World world;
	private Camera camera;

	// Disposable
	private SpriteBatch batch;

	// My stuff 
	private ArrayList<Character> ennemies = new ArrayList<Character>();
	private Character pony;
	private Vector2 vWorld = new Vector2(10, 10);
	private float w2p;


	@Override
	public void show(){
		world = new World(new Vector2(0, 0), true);
		batch = new SpriteBatch();


		// Output 
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		w2p = Gdx.graphics.getWidth() / vWorld.x;

		pony = new Character(this, WHO.ME);
	}

	@Override
	public void render(float delta){
		// Update 
		world.step(delta, 6, 2);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		// Render
		Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
			pony.draw(batch);
			for (Character ennemy : ennemies){
				ennemy.draw(batch);
			}
		batch.end();
		
	}


	@Override
	public void dispose() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}



	public class Character{
		BabyPony parent;
		Sprite sprite;
		float x = 0, y = 0;
		float angle;
		float sizeWorld = 1;
		float sizePixel;
		Vector2 vForce = new Vector2(0, 0);

		Body body;

		public Character(BabyPony parent, WHO who){
			this.parent = parent;
			sizePixel = sizeWorld * parent.w2p;

			// Sprite 
			sprite = new Sprite(new Texture("pony/pony_pink.png"));
			sprite.setSize(sizePixel / 2, sizePixel);
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

			// Body 
			createBody(parent.world);
		}

		public void createBody(World world){
			// body definition
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.set(0, 0);
			// BodyShape 
			CircleShape shape = new CircleShape();
			shape.setRadius(sizeWorld);
			// BodyFixture 
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = shape;
			fixtureDef.restitution = 1;
			// Create Body 
			body = world.createBody(bodyDef);
			body.createFixture(fixtureDef);
			// Add Body Sprite 
			body.setUserData(this); 
		}

		public void draw(SpriteBatch batch){
			// Get input
			inputGetter();

			// Apply force
			body.applyForceToCenter(vForce.x, vForce.y, true);

			// Get position 
			Vector2 pos = body.getPosition();
			x = pos.x * parent.w2p;
			y = pos.y * parent.w2p;

			// Draw 
			sprite.setPosition(x, y);
			sprite.setRotation(angle);
			sprite.draw(batch);
		}

		public void inputGetter(){
			int iForce = 5;
			if (Gdx.input.isKeyPressed(Keys.UP)){
				vForce = new Vector2(0, iForce);
				angle = 0;
				return;
			}

			if (Gdx.input.isKeyPressed(Keys.DOWN)){
				vForce = new Vector2(0, -iForce);
				angle = 180;
				return;
			}

			if (Gdx.input.isKeyPressed(Keys.LEFT)){
				vForce = new Vector2(-iForce, 0);
				angle = 90;
				return;
			}

			if (Gdx.input.isKeyPressed(Keys.RIGHT)){
				vForce = new Vector2(iForce, 0);
				angle = -90;
				return;
			}
		}


	}
}
