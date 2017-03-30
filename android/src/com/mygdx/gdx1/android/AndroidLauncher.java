package com.mygdx.gdx1.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.gdx1.Activity1;
import com.mygdx.gdx1.Tests;
import com.mygdx.gdx1.TestsNotGwt;

import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestsNotGwt.fillClassList(Tests.getClassList());
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Activity1(), config);
	}
}
