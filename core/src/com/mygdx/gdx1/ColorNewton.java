package com.mygdx.gdx1;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class ColorNewton implements Screen {
	ScrollPane scrollPane;
	Table table;

	// Disposable
	Stage stage;
	ImageButton imageHue, imageLs;
	SkinLib skinLib;
	Color colorCurrent, colorOpposed;
	TextField textCurrentColor;


	@Override
	public void show(){
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skinLib = new SkinLib();


		textCurrentColor = new TextField("0x323232", skinLib.getTextField());

		// Hue
		ImageButtonStyle styleHue = new ImageButtonStyle();
		styleHue.up = PixmapFactory.drawableFromPixmap(pixmapHue(100, 100));
		imageHue = new ImageButton(styleHue);


		// Saturation, Lightness
		ImageButtonStyle styleLs = new ImageButtonStyle();
		styleLs.up = PixmapFactory.drawableFromPixmap(pixmapLs(100, 100, Color.RED));
		imageLs = new ImageButton(styleLs);


		// PACK
		table = new Table();
		table.add(textCurrentColor).expandX().colspan(2).row();
		table.add(imageHue).expandX();
		table.add(imageLs).expandX().row();

		// SCROLLPANE
		scrollPane = new ScrollPane(table);
		scrollPane.setFillParent(true);
		stage.addActor(scrollPane);
		stage.setScrollFocus(scrollPane);
	}



	@Override
	public void render(float delta){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose(){
		stage.dispose();
		skinLib.dispose();
		// TODO dipose image pixmaps
	}





	@Override
	public void hide() { }

	@Override
	public void pause() { }

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		table.setWidth(Gdx.graphics.getWidth());
	}

	@Override
	public void resume() { }




	public static Pixmap pixmapLs(int width, int height, Color color){
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		float hue = rgbToHsl(Color.BLUE)[0];
		for (int y = 0; y < height; y++){
			float saturation = 1.0f * y / height;
			for (int x = 0; x < width; x++){
				float value = 1.0f * x / width;
				Color colorLs = hsvColor(hue, saturation, value);
				pixmap.drawPixel(x, height - 1 - y, Color.rgba8888(colorLs));
			}
		}
		return pixmap;
	}


	public static Pixmap pixmapHue(int width, int height){
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		// Flat
		for (int y = 0; y < height; y++){
			float hue = 1.0f * y;
			hue /= height;
			Color colorHue = hsvColor(hue, 0.9f, 0.99f);
			for (int x = 0; x < width; x++){
				pixmap.drawPixel(x, y, Color.rgba8888(colorHue));
			}
		}
		return pixmap;
	}


	public static Color hsvColor(float hue, float saturation, float value) {

		int h = (int)(hue * 6);
		float f = hue * 6 - h;
		float p = value * (1 - saturation);
		float q = value * (1 - f * saturation);
		float t = value * (1 - (1 - f) * saturation);

		switch (h) {
		  case 0: return new Color(value, t, p, 1);
		  case 1: return new Color(q, value, p, 1);
		  case 2: return new Color(p, value, t, 1);
		  case 3: return new Color(p, q, value, 1);
		  case 4: return new Color(t, p, value, 1);
		  case 5: return new Color(value, p, q, 1);
		  default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
		}
	}


	public static float[] rgbToHsl(Color color){
    	float r = color.r;
		float g = color.g;
		float b = color.b;
    	float max = Math.max(r, Math.max(g, b));
		float min = Math.min(r, Math.max(g, b));
    	float h = (max + min) / 2;
		float s = h;
		float l = h;

    	if(max == min){
    	    h = s = 0; // achromatic
    	}
		else{
    	    float d = max - min;
    	    s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
    	    if (max == r){
    	    	h = (g - b) / d + (g < b ? 6 : 0);
			}
			else if (max == g){
    	        h = (b - r) / d + 2;
			}
			else{
    	        h = (r - g) / d + 4;
    	    }
    	    h /= 6;
    	}

		float res[] = {h, s, l};
    	return res;
	}

}
