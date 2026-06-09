package sokobanrg;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class SokobanRG {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Sokoban RG");
        config.setWindowedMode(800, 600);
        config.useVsync(true);
        new Lwjgl3Application(new SokobanJuego(), config);
    }
}
