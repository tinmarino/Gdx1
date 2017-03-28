package com.mygdx.gdx1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.mygdx.gdx1.bullet.BulletTestCollision;

public class Tests{

	private static List<Class<? extends Screen>> classList = 
		new ArrayList<Class<? extends Screen>>(Arrays.asList(
		BabyPony.class,
		HyperCube.class
	));

	

	public static final List<Screen> tests = new ArrayList<Screen>();

	public static void  init(){
		if (Gdx.app.getType() !=  Application.ApplicationType.WebGL){
			classList.addAll( Arrays.asList(
				NetInput.class,
				Networking.class,
				Basic3DTest.class,
				LoadModelTest.class,
				LoadSceneTest.class,
				LoadSceneTest2.class,
				BehindTheSceneTest.class,
				ShaderTest.class,
				HyperCube.class,
				BulletTestCollision.class
			));
		}
	}

	public static Screen screenFromClass(Class<? extends Screen> screenClass){
		Screen screen = screenClass.cast(Screen.class);
		return screen;
	}


	public static ArrayList<String> getNames(){
		ArrayList<String> screenNames = new ArrayList<String>();
		for (Class<? extends Screen> screenClass : Tests.classList){
			String screenName = ClassReflection.getSimpleName(screenClass);
			screenNames.add(screenName);
		}
		return screenNames;
	}


	public static Screen getScreenFromName (String name) {
		Screen screen = null;
		for (Class<? extends Screen> clazz : classList)
			if (clazz.getSimpleName().equals(name)){
				try {
					screen = (Screen) ClassReflection.newInstance(clazz);
				} catch (ReflectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		return screen;
	}
}

