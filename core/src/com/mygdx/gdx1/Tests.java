package com.mygdx.gdx1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.reflect.ClassReflection;

/*
 	The list of classes I can load on all platforms.
	The classes to be loaded without GWT are in TestsNotGwt Which is loaded by the 
		desktopLauncher or ANdroidLauncher
*/

public class Tests{

	private static List<Class<? extends Screen>> classList = 
		new ArrayList<Class<? extends Screen>>(Arrays.asList(
		GpsSpeed.class,
		Compass.class,
		UiColorTest.class,
		BabyPony.class,
		ColorNewton.class
	));

	public static List<Class<? extends Screen>> getClassList(){
		return classList;
	}

	public static void initGwt(){}

	// Called by Platforms Laucnher
	public static void init(){}

	// For the Button Label
	public static ArrayList<String> getNames(){
		ArrayList<String> screenNames = new ArrayList<String>();
		for (Class<? extends Screen> screenClass : Tests.classList){
			String screenName = ClassReflection.getSimpleName(screenClass);
			screenNames.add(screenName);
		}
		return screenNames;
	}


	// Instanciator
	public static Screen getScreenFromName (String name) {
		Screen screen = null;
		for (Class<? extends Screen> clazz : classList){
			Gdx.app.log("GDX1 : ", "Class name is in loop :" + clazz.getSimpleName());
			if (clazz.getSimpleName().equals(name)){
				try {
					screen = (Screen) ClassReflection.newInstance(clazz);
				} catch (Exception e) {
					Gdx.app.log("GDX1 : ", " " + e.getLocalizedMessage() + e.getMessage() +  e.getStackTrace());
				}
			}
		}
		return screen;
	}
}
