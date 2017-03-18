package com.mygdx.gdx1;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class BabyPony implements Screen {
	enum WHO{ME, YOU}
	private World world;
	private Camera camera;

	// Disposable
	private SpriteBatch batch;
	private Sprite background;
	private Texture textureEnnemy = new Texture("pony/pony_blue.png");
	private Texture texturePony = new Texture("pony/pony_pink.png");
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(); 

	// My stuff
	private ArrayList<Character> ennemies = new ArrayList<Character>();
	private Character pony;
	private Vector2 vWorld = new Vector2(10, 10);
	private float w2p;
	private Random rand = new Random();


	@Override
	public void show(){
		w2p = Gdx.graphics.getWidth() / vWorld.x;
		world = new World(new Vector2(0, 0), true);
		batch = new SpriteBatch();

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		pony = new Character(this, WHO.ME);
		background = new Sprite(new Texture("pony/rink.png"));
		background.setSize(0.8f * w2p * vWorld.x, 0.4f * w2p * vWorld.y);
		background.setPosition(-background.getWidth() / 2, -background.getHeight() / 2);
	}

	@Override
	public void render(float delta){
		// Update 
		world.step(delta, 6, 2);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		createEnnemy();
		checkDead();

		// Render
		Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
			background.draw(batch);
			pony.draw(batch);
			for (Character ennemy : ennemies){
				ennemy.draw(batch);
			}
		batch.end();
		
		// Debug
		// Matrix4 debugMatrix=new Matrix4(camera.combined);
		// debugMatrix.scale(w2p, w2p, 1f);
		// debugRenderer.render(world, debugMatrix);
	}


	public void createEnnemy(){
		if (rand.nextInt(50) != 0){
			return;
		}
		Character ennemy = new Character(this, WHO.YOU);
		float y = 0.2f * (rand.nextFloat() -0.5f) * vWorld.y;
		float x = -0.4f * vWorld.x;
		ennemy.body.setTransform(new Vector2(x, y), 0);
		ennemy.body.applyForceToCenter(new Vector2(50, 0), true);
		ennemies.add(ennemy);
	}

	public void checkDead(){
		float x = pony.body.getPosition().x;
		float y = pony.body.getPosition().y;
		boolean bol = x < -0.4f * vWorld.x;
		bol |= x > 0.4f * vWorld.x;
		bol |= y < -0.2f * vWorld.y;
		bol |= y > 0.2f * vWorld.y;

		if (bol){
			pony.body.setTransform(0, 0, 0);
			pony.body.setLinearVelocity(0, 0);
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		background.getTexture().dispose();
		textureEnnemy.dispose();
		texturePony.dispose();
		debugRenderer.dispose(); 
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
		WHO who;
		float x = 0, y = 0;
		float angle;
		float sizeWorld = 1;
		float sizePixel;
		Vector2 vForce = new Vector2(0, 0);

		Body body;

		public Character(BabyPony parent, WHO who){
			this.parent = parent;
			this.who = who;
			sizePixel = sizeWorld * parent.w2p;

			// Sprite 
			if (who == WHO.ME){
				sprite = new Sprite(parent.texturePony);
			}
			else{
				sprite = new Sprite(parent.textureEnnemy);
				angle = -90;
			}
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
			shape.setRadius(sizeWorld / 2);
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
			if (who == WHO.ME){
				inputGetter();
			}

			// Apply force
			body.applyForceToCenter(vForce.x, vForce.y, true);

			// Get position 
			Vector2 pos = body.getPosition();
			x = pos.x * parent.w2p - sprite.getWidth() / 2;
			y = pos.y * parent.w2p - sprite.getHeight() / 2;

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
			vForce = new Vector2(0, 0);
			return;
		}


	}
}
