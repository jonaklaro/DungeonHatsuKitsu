package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.awt.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setTitle("Praktikum");
    config.setWindowedMode(1280, 720);
    config.setResizable(false);
    config.setIdleFPS(144);
    config.useVsync(true);
    config.useOpenGL3(true, 3, 2);
		//Grundlegende Einstellungen (FPS, Icon, Bildgröße)
		config.setForegroundFPS(144);
		config.setWindowedMode(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height);
		config.setWindowIcon("anderes.png");
		new Lwjgl3Application(new Praktikum(), config);
	}
}
