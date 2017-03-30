package com.mygdx.gdx1;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;

public class SkinLib{
	Drawable drawable;
	BitmapFont font; 
	Color colorFont, colorBackground, colorButton, colorSelection;
	Color colorButtonUp, colorButtonDown;




	public void dispose(){
		font.dispose();
	}

	public SkinLib(){
		initDefault();
	}

	public void initDefault(){
		colorFont 			= Color.BLACK;
		colorBackground 	= Color.CYAN;
		colorButton 		= Color.BLUE;
		colorSelection 		= Color.WHITE;
		colorButtonUp 		= Color.GREEN;
		colorButtonDown 	= Color.BLUE;
		initFont();
	}	
	public void initFont(){
		font = getFont();
	}

	public Skin getStandardSkin(){
		Skin skin = new Skin();
		return skin;


	}

	public static BitmapFont getFont(){
		return new BitmapFont(Gdx.files.internal("font/open_sans_std_32.fnt"));
	}


	// Sofisticated styles
	public ImageButtonStyle getImageButtonRoundPlay(int radius, Color color){
		ImageButtonStyle style = new ImageButtonStyle(); 
		Pixmap pixmapUp  	= PixmapFactory.circle(radius, color);
		Pixmap pixmapDown  	= PixmapFactory.circle(radius, colorButtonDown);
		PixmapFactory.play(pixmapUp, Color.WHITE);
		style.up = PixmapFactory.drawableFromPixmap(pixmapUp);
		style.down = PixmapFactory.drawableFromPixmap(pixmapDown);
		return style;
	}

	public ImageButtonStyle getImageButtonRoundStat(int radius, Color color){
		ImageButtonStyle style = new ImageButtonStyle(); 
		Pixmap pixmapUp  	= PixmapFactory.circle(radius, color);
		Pixmap pixmapDown  	= PixmapFactory.circle(radius, colorButtonDown);
		PixmapFactory.stat(pixmapUp, Color.WHITE);
		style.up = PixmapFactory.drawableFromPixmap(pixmapUp);
		style.down = PixmapFactory.drawableFromPixmap(pixmapDown);
		return style;
	}

	public ImageButtonStyle getImageButtonRoundMusic(int radius, Color color){
		ImageButtonStyle style = new ImageButtonStyle(); 
		Pixmap pixmapUp  	= PixmapFactory.circle(radius, color);
		Pixmap pixmapDown  	= PixmapFactory.circle(radius, colorButtonDown);
		PixmapFactory.music(pixmapUp, Color.WHITE);
		style.up = PixmapFactory.drawableFromPixmap(pixmapUp);
		style.down = PixmapFactory.drawableFromPixmap(pixmapDown);
		return style;
	}

	public ImageButtonStyle getImageButtonRoundHouse(int radius, Color color){
		ImageButtonStyle style = new ImageButtonStyle(); 
		Pixmap pixmapUp  	= PixmapFactory.circle(radius, color);
		Pixmap pixmapDown  	= PixmapFactory.circle(radius, colorButtonDown);
		PixmapFactory.house(pixmapUp, Color.WHITE, color);
		style.up = PixmapFactory.drawableFromPixmap(pixmapUp);
		style.down = PixmapFactory.drawableFromPixmap(pixmapDown);
		return style;
	}
	//
	// STYLES from AES encryptor
	//

	// Sure you dont need a table (static, no font)? 
	public WindowStyle getWindow(){
		WindowStyle style = new WindowStyle();
		style.titleFont = font;
		return style;
 		///** Optional. */
		//public Drawable background;
		//public BitmapFont titleFont;
		///** Optional. */
		//public Color titleFontColor = new Color(1, 1, 1, 1);
		///** Optional. */
		//public Drawable stageBackground;
	}


	public ImageButtonStyle getImageButton(){
		ImageButtonStyle style = new ImageButtonStyle(); 
		style.up = PixmapFactory.getDrawableMonocromatic(1,1,new Color(1,1,1, 0.01f), null);
		style.down = PixmapFactory.getDrawableMonocromatic(1,1,new Color(1,1,1,0.5f), null);
		return style;
	}


	public TextFieldStyle getTextField(List<Disposable> disposableList){
		TextFieldStyle style 	= new TextFieldStyle(); 
		style.font 				= font;
		style.fontColor 		= colorFont;
		style.background 		= PixmapFactory.getDrawableMonocromatic(1, 1, colorBackground, disposableList);
		style.cursor			= PixmapFactory.getDrawableMonocromatic(2, 16, colorFont , null);
		style.selection			= PixmapFactory.getDrawableMonocromatic(2, 16, colorSelection, disposableList);
		return style;
	}

	public LabelStyle getLabel(){
		LabelStyle style 		= new LabelStyle();
		style.font 				= font; 
		style.fontColor 		= colorFont;
		return style;
	}

	public TextButtonStyle getTextButton(){
		TextButtonStyle style 	= new TextButtonStyle();
		style.font 				= font; 
		style.fontColor 		= colorFont;
		style.up 				= getDrawableUp();
		style.down 				= getDrawableDown();
		return style;
	}


	public ButtonStyle getButton(){
		ButtonStyle style 		= new ButtonStyle();
		style.up 				= getDrawableUp();
		style.down 				= getDrawableDown();
		return style;
	}

	public Drawable getDrawableUp(){
		Texture t1 				= new Texture(PixmapFactory.circle(32, colorButtonUp));
		return PixmapFactory.ninePatchFromTexture(t1);
	}

	public Drawable getDrawableDown(){
		Texture t2 				= new Texture(PixmapFactory.circle(32, colorButtonDown));
		return PixmapFactory.ninePatchFromTexture(t2); 
	}

	





}
