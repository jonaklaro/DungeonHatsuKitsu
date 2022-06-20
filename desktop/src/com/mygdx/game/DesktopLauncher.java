package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.awt.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
  public static void main(String[] arg) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setTitle("Acrobat Dungeon");
    float windowScaleFactor = 0.3f;
    int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * windowScaleFactor);
    int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * windowScaleFactor);
    config.setWindowedMode(x, y);
    config.setWindowPosition(x, y);
    config.setIdleFPS(0);
    // config.setIdleFPS(144);
    new Lwjgl3Application(new AcrobatDungeon(), config);
  }
}
