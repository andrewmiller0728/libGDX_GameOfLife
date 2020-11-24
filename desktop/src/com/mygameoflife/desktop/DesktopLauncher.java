package com.mygameoflife.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygameoflife.MyGameOfLife;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1536;
		config.height = 1536;
		new LwjglApplication(new MyGameOfLife(), config);
	}
}
