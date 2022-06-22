package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.awt.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
      public static void main(String[] arg) {
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setTitle("Acrobat Dungeon");
            float windowScaleFactor = 0.7f;
            int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * windowScaleFactor);
            int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * windowScaleFactor);
            config.setWindowedMode(x, y);
            config.setWindowPosition(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - x / 2,
                        Toolkit.getDefaultToolkit().getScreenSize().height / 2 - y / 2);
            config.setResizable(false);
            config.setForegroundFPS(60);
            new Lwjgl3Application(new AcrobatDungeon(), config);
      }
}
