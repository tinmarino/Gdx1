/*
 *  Excluded for html in html/src/com/mygdx/gdx1/GdxDefinition.gwt.xml
 */
package com.mygdx.gdx1;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.mygdx.gdx1.bullet.BulletTestCollision;
import com.mygdx.gdx1.bullet.HyperCube;
import com.mygdx.gdx1.net.NetInput;
import com.mygdx.gdx1.net.Networking;

public class TestsNotGwt{

	public static void fillClassList(List<Class<? extends Screen>> classList){
		classList.addAll(Arrays.asList(

			RayPickingTest.class,
			FrustumCullingTest.class,
			MaterialTest.class,
			HyperCube.class,
			NetInput.class,
			Networking.class,
			Basic3DTest.class,
			LoadModelTest.class,
			LoadSceneTest.class,
			LoadSceneTest2.class,
			BehindTheSceneTest.class,
			ShaderTest.class,
			HyperCube.class,
			BulletTestCollision.class,

			ChampLibre.class,
			NewYork.class
		));
	}
}
