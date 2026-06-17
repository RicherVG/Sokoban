package com.sokoban;
/**
 *
 * @author riche
 */

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Sokoban RG");
        config.setWindowedMode(900, 650);
        config.useVsync(true);
        config.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public boolean closeRequested() {
                System.exit(0);
                return true;
            }
        });
        new Lwjgl3Application(new SokobanJuego(), config);
    }
}

