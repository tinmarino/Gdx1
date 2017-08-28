package com.mygdx.gdx1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ShaderClass implements Shader {
	// The shader program expects two uniforms u_worldTrans and u_projViewTrans. The latter depends only on the camera, which means we can set that within the begin method:
	ShaderProgram program;
	private Camera camera;
	private RenderContext context;
	private int u_projViewTrans;
	private int u_worldTrans;


    @Override
    public void init () {
		String vert = Gdx.files.internal("data/test.vertext.glsl").readString();
		String frag = Gdx.files.internal("data/test.fragment.glsl").readString();
		program = new ShaderProgram(vert, frag);
		if (!program.isCompiled())
			throw new GdxRuntimeException(program.getLog());

		// Enable to cache string in the gpu memory
        u_projViewTrans = program.getUniformLocation("u_projViewTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
	}
    @Override
    public void dispose () {
		program.dispose();
	}

	// The begin method will be called every frame when the shader is about to be used for rendering one or more renderables
	// The begin method has two arguments, camera and context, which will be exclusive (and not changed) for our shader until the end method is called
    @Override
    public void begin (Camera camera, RenderContext context) {
		this.camera = camera;
		this.context = context;
		program.begin();
		// Caching too
		// program.setUniformMatrix("u_projViewTrans", camera.combined);
        program.setUniformMatrix(u_projViewTrans, camera.combined);
		// Softer
		context.setDepthTest(GL20.GL_LEQUAL);
		// Do not see what not face camera
        context.setCullFace(GL20.GL_BACK);
	}
    @Override
    public void render (Renderable renderable) { 
		// The u_worldTrans depends on the renderable, so we have to set that value within the render() 
        // program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
		// Caching faaster access
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        renderable.meshPart.render(program);
	}

	// The end() method is called every frame when the rendering with the shader is ready.
    @Override
    public void end () {
		program.end();
	}

	// The compareTo method is used by ModelBatch to decide which shader should be used first, we will not use that for now
    @Override
    public int compareTo (Shader other) {
        return 0;
    }

	// The canRender method will be used to decide the shader should be used to render the specified renderable,
    @Override
    public boolean canRender (Renderable instance) {
        return true;
    }
}
