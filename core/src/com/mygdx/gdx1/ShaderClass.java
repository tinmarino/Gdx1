package com.mygdx.gdx1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ShaderClass implements Shader {
    public static class TestColorAttribute extends ColorAttribute {
        public final static String DiffuseUAlias = "diffuseUColor";
        public final static long DiffuseU = register(DiffuseUAlias);

        public final static String DiffuseVAlias = "diffuseVColor";
        public final static long DiffuseV = register(DiffuseVAlias);

        static {
            Mask = Mask | DiffuseU | DiffuseV;
        }

        public TestColorAttribute (long type, float r, float g, float b, float a) {
            super(type, r, g, b, a);
        }
    }
	

	// The shader program expects two uniforms u_worldTrans and u_projViewTrans. The latter depends only on the camera, which means we can set that within the begin method:
	ShaderProgram program;
	private Camera camera;
	private RenderContext context;
	private int u_projViewTrans;
	private int u_worldTrans;
	private int u_color;
	private int u_colorU;
	private int u_colorV;


    @Override
    public void init () {
		// Not 2 for ShaderTEst
		String vert = Gdx.files.internal("data/test.vertext2.glsl").readString();
		String frag = Gdx.files.internal("data/test.fragment2.glsl").readString();
		program = new ShaderProgram(vert, frag);
		if (!program.isCompiled())
			throw new GdxRuntimeException(program.getLog());

		// Enable to cache string in the gpu memory
        u_projViewTrans = program.getUniformLocation("u_projTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_color = program.getUniformLocation("u_color");
        u_colorU = program.getUniformLocation("u_colorU");
        u_colorV = program.getUniformLocation("u_colorV");
		// Added for material 
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
        // Color color = (Color)renderable.userData;
        Color colorU = ((ColorAttribute)renderable.material.get(TestColorAttribute.DiffuseU)).color;
        Color colorV = ((ColorAttribute)renderable.material.get(TestColorAttribute.DiffuseV)).color;
        program.setUniformf(u_colorU, colorU.r, colorU.g, colorU.b);
        program.setUniformf(u_colorV, colorV.r, colorV.g, colorV.b);
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
        // return instance.material.has(ColorAttribute.Diffuse);
        return instance.material.has(TestColorAttribute.DiffuseU | TestColorAttribute.DiffuseV);
    }
}
