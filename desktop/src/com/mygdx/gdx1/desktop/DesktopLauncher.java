package com.mygdx.gdx1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.gdx1.Activity1;
import com.mygdx.gdx1.GpsInterface;
import com.mygdx.gdx1.Tests;
import com.mygdx.gdx1.TestsNotGwt;

public class DesktopLauncher {
	public static void main (String[] arg) {
		TestsNotGwt.fillClassList(Tests.getClassList());
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Math.round(320 * 1.f);
		config.height = Math.round(480 * 1.f);
		GpsInterface.Container.Instance = null;
		new LwjglApplication(new Activity1(), config);
	}
}
