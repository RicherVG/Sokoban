package sokobanrg.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import sokobanrg.SokobanJuego;
import sokobanrg.model.Usuario;

public class PantallaSeleccionNivel extends PantallaBase {

    public PantallaSeleccionNivel(SokobanJuego juego) {
        super(juego);
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

        Label lblTitulo = crearEtiqueta("Selecciona un Nivel", true);
        tablaPanel.add(lblTitulo).padBottom(40).row();

        Usuario usuario = juegoSokoban.getUsuarioActual();
        int maxNivel = (usuario != null) ? usuario.getNivelMaximoDesbloqueado() : 1;

        Table tablaNiveles = new Table();
        for (int i = 1; i <= 5; i++) {
            final int nivel = i;
            TextButton btnNivel = crearBoton("Nivel " + nivel);
            
            if (nivel > maxNivel) {
                btnNivel.setDisabled(true);
                btnNivel.setText("Bloqueado"); 
            } else {
                btnNivel.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        juegoSokoban.mostrarPantallaJuego(nivel);
                    }
                });
            }
            tablaNiveles.add(btnNivel).width(120).height(60).pad(10);
        }

        tablaPanel.add(tablaNiveles).padBottom(40).row();

        TextButton btnVolver = crearBoton("Volver al Menú");
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaMenu();
            }
        });

        tablaPanel.add(btnVolver).width(240).height(48);

        tablaBorde.add(tablaPanel).pad(2);
        tablaExterior.add(tablaBorde);
    }
}
