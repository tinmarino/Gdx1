package com.mygdx.gdx1.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.gdx1.Activity1;
import com.mygdx.gdx1.GpsInterface;
import com.mygdx.gdx1.Tests;
import com.mygdx.gdx1.TestsNotGwt;

import android.os.Bundle;
import android.util.Log;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		Log.i("Gdx1","Android Activity starting");
		super.onCreate(savedInstanceState);
		TestsNotGwt.fillClassList(Tests.getClassList());
		GpsInterface.Container.Instance = new GpsAndroid(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Activity1(), config);
	}
}
