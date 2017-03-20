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
	private Texture textureEnnemy;
	private Texture texturePony;
	private Box2DDebugRenderer debugRenderer; 

	// My stuff
	private boolean debug = false;
	private ArrayList<Character> ennemies = new ArrayList<Character>();
	private Vector2 vWorld = new Vector2(10, 10);
	private Vector2 vGame = new Vector2(8, 4);
	private Random rand = new Random();
	private Character pony;
	private float w2p;


	@Override
	public void show(){
		// Standard
		w2p = Gdx.graphics.getWidth() / vWorld.x;
		world = new World(new Vector2(0, 0), true);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		debugRenderer = new Box2DDebugRenderer();

		// Images
		texturePony = new Texture("pony/pony_pink.png");
		textureEnnemy = new Texture("pony/pony_blue.png");
		background = new Sprite(new Texture("pony/rink.png"));
		background.setSize(w2p * vGame.x,  w2p * vGame.y);
		background.setPosition(-background.getWidth() / 2, -background.getHeight() / 2);

		// Character
		pony = new Character(this, WHO.ME);
	}

	@Override
	public void render(float delta){
		// Update Standard
		world.step(delta, 6, 2);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		// Update My physics
		pony.update(delta);
		createEnnemy();
		checkDead();

		// Render
		Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
			background.draw(batch);
			for (Character ennemy : ennemies){
				ennemy.draw(batch);
				ennemy.update(delta);
			}
			pony.draw(batch);
		batch.end();
		
		// Debug
		if (debug){
			Matrix4 debugMatrix=new Matrix4(camera.combined);
			debugMatrix.scale(w2p, w2p, 1f);
			debugRenderer.render(world, debugMatrix);
		}
	}

	@Override
	public void dispose(){
		batch.dispose();
		background.getTexture().dispose();
		textureEnnemy.dispose();
		texturePony.dispose();
		debugRenderer.dispose(); 
	}

	@Override
	public void hide(){}

	@Override
	public void pause(){}

	@Override
	public void resize(int width, int height){}

	@Override
	public void resume(){}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Utils 
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void createEnnemy(){
		if (rand.nextInt(50) != 0){
			return;
		}
		Character ennemy = new Character(this, WHO.YOU);
		float y = (rand.nextFloat() - 0.5f) * vGame.y;
		float x = - 0.5f * vGame.x;
		ennemy.body.setTransform(new Vector2(x, y), 0);
		ennemy.body.applyForceToCenter(new Vector2(50, 0), true);
		ennemies.add(ennemy);
	}

	public void checkDead(){
		// Check
		float x = pony.body.getPosition().x;
		float y = pony.body.getPosition().y;
		boolean bol = x < -0.5f * vGame.x;
		bol |= x > 0.5f * vGame.x;
		bol |= y < -0.5f * vGame.y;
		bol |= y > 0.5f * vGame.y;

		// Dye
		if (bol){
			pony.body.setTransform(0, 0, 0);
			pony.body.setLinearVelocity(0, 0);
		}
	}


	public class Character{
		BabyPony parent;
		Body body;
		Sprite sprite;
		Vector2 vForce = new Vector2(0, 0);
		// Parameters
		WHO who;
		float angle = 0, sizeWorld = 1, sizePixel;


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
			// Create Body
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			body = world.createBody(bodyDef);

			// Add Fixture
			CircleShape shape = new CircleShape();
			shape.setRadius(sizeWorld / 2);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = shape;
			fixtureDef.restitution = 1;
			body.createFixture(fixtureDef);
		}

		public void update(float delta){
			// Get input
			if (who == WHO.ME){
				inputGetter();
			}

			// Apply force
		}

		public void draw(SpriteBatch batch){
			// Get position 
			Vector2 pos = body.getPosition();
			float x = pos.x * parent.w2p - sprite.getWidth() / 2;
			float y = pos.y * parent.w2p - sprite.getHeight() / 2;

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
			}

			else if (Gdx.input.isKeyPressed(Keys.DOWN)){
				vForce = new Vector2(0, -iForce);
				angle = 180;
			}

			else if (Gdx.input.isKeyPressed(Keys.LEFT)){
				vForce = new Vector2(-iForce, 0);
				angle = 90;
			}

			else if (Gdx.input.isKeyPressed(Keys.RIGHT)){
				vForce = new Vector2(iForce, 0);
				angle = -90;
			}

			else{
				vForce = new Vector2(0, 0);
			}

			body.applyForceToCenter(vForce.x, vForce.y, true);
			return;
		}


	}
}
