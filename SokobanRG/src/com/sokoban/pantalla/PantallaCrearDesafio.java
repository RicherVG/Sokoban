package com.sokoban.pantalla;

/**
 * @author riche
 */

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sokoban.SokobanJuego;
import com.sokoban.model.Desafio;
import com.sokoban.model.Usuario;

public class PantallaCrearDesafio extends PantallaBase {

    private int nivelSeleccionado = 1;
    private Label lblNivelSeleccionado;
    private boolean desafioSinVidas = false;

    public PantallaCrearDesafio(SokobanJuego juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();

        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();
        if (skin == null) return;

        Usuario usuario = juegoSokoban.getUsuarioActual();
        if (usuario == null) {
            juegoSokoban.mostrarPantallaLogin();
            return;
        }

        String idioma = usuario.getIdioma();

        Table tablaRaiz = new Table();
        tablaRaiz.setFillParent(true);
        escenario.addActor(tablaRaiz);

        Table tablaPanel = new Table();
        tablaPanel.setBackground(skin.getDrawable("panel-bg"));
        tablaPanel.pad(30, 40, 30, 40);

        Label titulo = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("crear_desafio", idioma), skin, "title");

        Label lblOponente = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("oponente", idioma), skin, "subtitle");
        final TextField campoOponente = crearCampoTexto("  Nombre de usuario del oponente");

        Label lblNivel = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("nivel_reto", idioma), skin, "subtitle");

        lblNivelSeleccionado = new Label("> Nivel 1", skin, "default");

        Table tablaNiveles = new Table();
        for (int i = 1; i <= 5; i++) {
            final int n = i;
            TextButton btnN = new TextButton("Niv " + i, skin, "secondary");
            btnN.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    nivelSeleccionado = n;
                    lblNivelSeleccionado.setText("> Nivel " + n);
                }
            });
            tablaNiveles.add(btnN).width(70).height(40).padRight(6);
        }

        TextButton btnCamp = new TextButton(juegoSokoban.getManejadorRecursos().obtenerTexto("campeonato", idioma), skin, "secondary");
        btnCamp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nivelSeleccionado = 0;
                lblNivelSeleccionado.setText("> " + juegoSokoban.getManejadorRecursos().obtenerTexto("campeonato", idioma));
            }
        });

        final Label etiquetaMensaje = new Label("", skin, "message");

        final TextButton btnSinVidasDesafio = new TextButton("Reto sin vidas: OFF", skin, "secondary");
        btnSinVidasDesafio.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                desafioSinVidas = !desafioSinVidas;
                btnSinVidasDesafio.setText(desafioSinVidas ? "Reto sin vidas: ON" : "Reto sin vidas: OFF");
                btnSinVidasDesafio.setStyle(skin.get(desafioSinVidas ? "default" : "secondary", TextButton.TextButtonStyle.class));
            }
        });

        TextButton btnEnviar = crearBoton("Enviar Desafio");
        TextButton btnVolver = new TextButton(juegoSokoban.getManejadorRecursos().obtenerTexto("volver", idioma), skin, "secondary");

        btnEnviar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String oponenteNombre = campoOponente.getText().trim();
                enviarDesafio(oponenteNombre, etiquetaMensaje, idioma);
            }
        });

        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaDesafios();
            }
        });

        tablaPanel.add(titulo).padBottom(20).row();
        tablaPanel.add(lblOponente).left().padBottom(5).row();
        tablaPanel.add(campoOponente).width(340).height(46).padBottom(20).row();
        tablaPanel.add(lblNivel).left().padBottom(8).row();
        tablaPanel.add(tablaNiveles).padBottom(6).row();
        tablaPanel.add(btnCamp).width(220).height(38).padBottom(10).row();
        tablaPanel.add(lblNivelSeleccionado).padBottom(10).row();
        tablaPanel.add(btnSinVidasDesafio).width(220).height(38).padBottom(14).row();
        tablaPanel.add(etiquetaMensaje).height(24).padBottom(12).row();
        tablaPanel.add(btnEnviar).width(260).height(48).padBottom(14).row();
        tablaPanel.add(btnVolver).width(200).height(42).row();

        tablaRaiz.add(tablaPanel).pad(10);
    }

    private void enviarDesafio(String oponenteNombre, Label etiquetaMensaje, String idioma) {
        Usuario yo = juegoSokoban.getUsuarioActual();
        if (yo == null) return;

        if (oponenteNombre.isEmpty()) {
            etiquetaMensaje.setText("Ingresa el nombre del oponente.");
            return;
        }

        if (oponenteNombre.equalsIgnoreCase(yo.getNombreUsuario())) {
            etiquetaMensaje.setText("No puedes retarte a ti mismo.");
            return;
        }

        if (!juegoSokoban.getAlmacenamiento().existeUsuario(oponenteNombre)) {
            etiquetaMensaje.setText("El usuario \"" + oponenteNombre + "\" no existe.");
            return;
        }

        Desafio nuevo = new Desafio(yo.getNombreUsuario(), oponenteNombre, nivelSeleccionado, desafioSinVidas);
        juegoSokoban.getAlmacenamiento().guardarDesafio(nuevo);

        etiquetaMensaje.setText("Desafio enviado a " + oponenteNombre + "!");

        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                juegoSokoban.mostrarPantallaDesafios();
            }
        }, 1.5f);
    }
}
