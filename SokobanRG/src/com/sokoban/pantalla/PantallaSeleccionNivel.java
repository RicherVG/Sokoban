package com.sokoban.pantalla;
/**
 *
 * @author riche
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sokoban.SokobanJuego;
import com.sokoban.model.Usuario;

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
        String idioma = juegoSokoban.getIdiomaActual();

        Table tablaExterior = new Table();
        tablaExterior.setFillParent(true);
        escenario.addActor(tablaExterior);

        Table tablaPanel = new Table();
        tablaPanel.setBackground(skin.getDrawable("panel-bg"));
        tablaPanel.pad(30, 40, 30, 40);

        String txtSeleccion = idioma.equals("en") ? "Select a Level" : "Selecciona un Nivel";
        Label lblTitulo = crearEtiqueta(txtSeleccion, true);
        tablaPanel.add(lblTitulo).padBottom(40).row();

        Usuario usuario = juegoSokoban.getUsuarioActual();
        int maxNivel = (usuario != null) ? usuario.getNivelMaximoDesbloqueado() : 1;

        String txtNivel  = idioma.equals("en") ? "Level "    : "Nivel ";
        String txtBloq   = idioma.equals("en") ? "Locked"    : "Bloqueado";
        String txtVolver = idioma.equals("en") ? "Back"      : "Volver al Menú";

        Table tablaNiveles = new Table();
        for (int i = 1; i <= 5; i++) {
            final int nivel = i;
            TextButton btnNivel = crearBoton(txtNivel + nivel);

            if (nivel > maxNivel) {
                btnNivel.setDisabled(true);
                btnNivel.setText(txtBloq);
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

        TextButton btnVolver = crearBoton(txtVolver);
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaMenu();
            }
        });

        tablaPanel.add(btnVolver).width(240).height(48);

        tablaExterior.add(tablaPanel).pad(3).center();
    }
}
