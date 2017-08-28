package com.mygdx.gdx1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

/*
 	Test some 4 color combinations for the website
	The UI to display what I want is UiTest
*/
public class UiColorTest implements Screen {
	Stage stage;
	SkinLib skinLib;
	Table table;

	private int screenWidth = Gdx.graphics.getWidth();

	@Override
	public void show() {
		stage = new Stage();
		skinLib = new SkinLib();
		Gdx.input.setInputProcessor(stage);

		table = new Table();
		ColorBrillant colorBrillant = new ColorBrillant();
		Method[] methods = ClassReflection.getMethods(ColorBrillant.class);
		for (Method method : methods){
			TextButton textButton = new TextButton(method.getName(), skinLib.getTextButton());
			textButton.getLabel().setAlignment(Align.left);
			try {
				method.invoke(colorBrillant);
			} catch (ReflectionException e) {
				Gdx.app.log("Gdx1", "Bad reflection in UiColorTests\n");
				e.printStackTrace();
			}
			Color colors[] =  {colorBrillant.color1, colorBrillant.color2, colorBrillant.color3, colorBrillant.color4};
			for (Color color : colors){
				Image image = new Image();
				Pixmap pixmap = PixmapFactory.monocromaticPixmap(1, 1, color);
				Drawable drawable = PixmapFactory.drawableFromPixmap(pixmap);
				image.setDrawable(drawable);
				table.add(image).width(screenWidth/16).fill().expand().left();
				
			}
			table.add(textButton).fillX().expandX().left().pad(5);
			table.row();
		}

		// ScrollPane
		ScrollPane scrollPane = new ScrollPane(table);
		scrollPane.setFillParent(true);
		stage.addActor(scrollPane);
		stage.setScrollFocus(scrollPane);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
		skinLib.dispose();
	}

	@Override
	public void hide() {}

	@Override
	public void pause() { }

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		table.invalidateHierarchy();
		table.setWidth(Gdx.graphics.getWidth());
	}

	@Override
	public void resume() { }

}
