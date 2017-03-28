package com.mygdx.gdx1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.gdx1.Activity1;
import com.mygdx.gdx1.Tests;
import com.mygdx.gdx1.TestsNotGwt;

public class DesktopLauncher {
	public static void main (String[] arg) {
		TestsNotGwt.fillClassList(Tests.getClassList());
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Activity1(), config);
	}
}
