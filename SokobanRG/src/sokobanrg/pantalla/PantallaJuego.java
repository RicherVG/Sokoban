package sokobanrg.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import sokobanrg.SokobanJuego;

public class PantallaJuego extends PantallaBase {
    private int nivelActual;

    public PantallaJuego(SokobanJuego juego, int nivel) {
        super(juego);
        this.nivelActual = nivel;
    }

    @Override
    public void show() {
        super.show();

        if (juegoSokoban.getManejadorRecursos().getSkinUI() == null) {
            return;
        }

        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();

        Table tablaExterior = new Table();
        tablaExterior.setFillParent(true);
        escenario.addActor(tablaExterior);

        Table tablaBorde = new Table();
        tablaBorde.setBackground(skin.getDrawable("border-bg"));

        Table tablaPanel = new Table();
        tablaPanel.setBackground(skin.getDrawable("panel-bg"));
        tablaPanel.pad(30, 40, 30, 40);

        Label lblTitulo = crearEtiqueta("Nivel " + nivelActual, true);
        tablaPanel.add(lblTitulo).padBottom(40).row();

        Label lblProximamente = crearEtiqueta("Lógica del juego próximamente...", false);
        tablaPanel.add(lblProximamente).padBottom(40).row();

        TextButton btnVolver = crearBoton("Volver a Selección");
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaSeleccionNivel();
            }
        });

        tablaPanel.add(btnVolver).width(240).height(48);

        tablaBorde.add(tablaPanel).pad(2);
        tablaExterior.add(tablaBorde);
    }
}
