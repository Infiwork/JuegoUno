package com.infiwork.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] argsf) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "JuegoUno";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 450;
		
		new LwjglApplication(new JuegoUno(), cfg);
	}
}
