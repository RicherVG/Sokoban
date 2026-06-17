package com.sokoban.pantalla;

/**
 * @author riche
 */

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sokoban.SokobanJuego;
import com.sokoban.model.Desafio;
import com.sokoban.model.EstadoDesafio;
import com.sokoban.model.Usuario;

public class PantallaResultadoDesafio extends PantallaBase {

    private final String desafioId;

    public PantallaResultadoDesafio(SokobanJuego juego, String desafioId) {
        super(juego);
        this.desafioId = desafioId;
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

        Desafio d = juegoSokoban.getAlmacenamiento().cargarDesafio(desafioId);
        if (d == null) {
            juegoSokoban.mostrarPantallaDesafios();
            return;
        }

        String idioma = usuario.getIdioma();
        String yo = usuario.getNombreUsuario();
        boolean soyDestinatario = yo.equals(d.getNombreDestinatario());

        Table tablaRaiz = new Table();
        tablaRaiz.setFillParent(true);
        escenario.addActor(tablaRaiz);

        Table tablaPanel = new Table();
        tablaPanel.setBackground(skin.getDrawable("panel-bg"));
        tablaPanel.pad(30, 40, 30, 40);

        Label titulo = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("resultado_desafio", idioma), skin, "title");
        tablaPanel.add(titulo).padBottom(10).row();

        Label lblDescripcion = new Label(d.getDescripcionNivel(), skin, "subtitle");
        tablaPanel.add(lblDescripcion).padBottom(20).row();

        if (d.getEstado() == EstadoDesafio.PENDIENTE) {
            Label lblEspera = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("pendiente_aceptar", idioma) + "\n" + d.getNombreDestinatario(), skin, "subtitle");
            tablaPanel.add(lblEspera).padBottom(30).row();

        } else if (d.getEstado() == EstadoDesafio.ACEPTADO) {
            String oponente = soyDestinatario ? d.getNombreRemitente() : d.getNombreDestinatario();
            Label lblEspera = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("esperar_oponente", idioma) + "\n" + oponente, skin, "subtitle");
            tablaPanel.add(lblEspera).padBottom(30).row();

        } else if (d.getEstado() == EstadoDesafio.B_JUGO) {
            if (soyDestinatario) {
                String oponente = d.getNombreRemitente();
                Label lblEspera = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("ya_jugaste", idioma) + " " + oponente, skin, "subtitle");
                String textoPuntaje = d.isSinVidas() ? "Partida sin puntajes" : "Tu puntaje: " + d.getPuntuacionDestinatario();
                Label lblPuntaje = new Label(textoPuntaje + "\n(El otro jugador está jugando aún...)", skin, "default");
                tablaPanel.add(lblEspera).padBottom(15).row();
                tablaPanel.add(lblPuntaje).padBottom(30).row();
            } else {
                Label lblTurno = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("tu_turno", idioma), skin, "title");
                tablaPanel.add(lblTurno).padBottom(10).row();

                Label lblInfo = new Label(d.getNombreDestinatario() + " ya jugó. ¡Ahora es tu turno!", skin, "subtitle");
                tablaPanel.add(lblInfo).padBottom(20).row();

                TextButton btnJugar = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("jugar_desafio", idioma));
                btnJugar.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Desafio actualizado = juegoSokoban.getAlmacenamiento().cargarDesafio(d.getId());
                        if (actualizado == null) return;
                        juegoSokoban.iniciarModoDesafio(actualizado);
                        int nivelInicio = actualizado.getNivel() == 0 ? 1 : actualizado.getNivel();
                        juegoSokoban.mostrarPantallaJuego(nivelInicio);
                    }
                });
                tablaPanel.add(btnJugar).width(220).height(48).padBottom(20).row();
            }

        } else if (d.getEstado() == EstadoDesafio.COMPLETADO) {
            tablaPanel.add(new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("ambos_jugaron", idioma), skin, "subtitle")).padBottom(20).row();

            Table tablaComparativa = new Table();
            tablaComparativa.setBackground(skin.getDrawable("border-bg"));
            tablaComparativa.pad(16);

            tablaComparativa.add(new Label("", skin, "default")).width(180);
            tablaComparativa.add(new Label(d.getNombreRemitente(), skin, "subtitle")).width(160).padRight(20);
            tablaComparativa.add(new Label(d.getNombreDestinatario(), skin, "subtitle")).width(160).row();

            if (d.isSinVidas()) {
                Label lblAviso = new Label("Partida sin puntajes (Modo Sin Vidas)", skin, "subtitle");
                lblAviso.setColor(com.badlogic.gdx.graphics.Color.ORANGE);
                tablaPanel.add(lblAviso).padBottom(20).row();
            } else {
                tablaComparativa.add(new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("puntuacion", idioma), skin, "default")).left();
                tablaComparativa.add(new Label(String.valueOf(d.getPuntuacionRemitente()), skin, "default")).center().padRight(20);
                tablaComparativa.add(new Label(String.valueOf(d.getPuntuacionDestinatario()), skin, "default")).center().row();
            }

            tablaComparativa.add(new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("tiempo", idioma), skin, "default")).left();
            tablaComparativa.add(new Label(d.getTiempoRemitente() + "s", skin, "default")).center().padRight(20);
            tablaComparativa.add(new Label(d.getTiempoDestinatario() + "s", skin, "default")).center().row();

            tablaComparativa.add(new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("movimientos", idioma), skin, "default")).left();
            tablaComparativa.add(new Label(String.valueOf(d.getMovimientosRemitente()), skin, "default")).center().padRight(20);
            tablaComparativa.add(new Label(String.valueOf(d.getMovimientosDestinatario()), skin, "default")).center().row();

            tablaPanel.add(tablaComparativa).padBottom(20).row();

            String nombreGanador = determinarGanador(d);
            if (nombreGanador == null) {
                tablaPanel.add(new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("empate", idioma), skin, "title")).padBottom(20).row();
            } else {
                String txtGanador = juegoSokoban.getManejadorRecursos().obtenerTexto("ganador", idioma) + " " + nombreGanador;
                if (nombreGanador.equals(yo)) {
                    txtGanador = "!! " + txtGanador + " ¡Eres tú!";
                }
                tablaPanel.add(new Label(txtGanador, skin, "title")).padBottom(20).row();
            }

        } else if (d.getEstado() == EstadoDesafio.RECHAZADO) {
            tablaPanel.add(new Label("El desafío fue rechazado.", skin, "subtitle")).padBottom(30).row();
        }

        TextButton btnVolver = new TextButton(juegoSokoban.getManejadorRecursos().obtenerTexto("volver", idioma), skin, "secondary");
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaDesafios();
            }
        });

        tablaPanel.add(btnVolver).width(220).height(44).row();
        tablaRaiz.add(tablaPanel).pad(10);
    }

    private String determinarGanador(Desafio d) {
        if (d.isSinVidas()) {
            if (d.getMovimientosRemitente() < d.getMovimientosDestinatario()) return d.getNombreRemitente();
            if (d.getMovimientosDestinatario() < d.getMovimientosRemitente()) return d.getNombreDestinatario();
            if (d.getTiempoRemitente() < d.getTiempoDestinatario()) return d.getNombreRemitente();
            if (d.getTiempoDestinatario() < d.getTiempoRemitente()) return d.getNombreDestinatario();
            return null;
        }
        int ptA = d.getPuntuacionRemitente();
        int ptB = d.getPuntuacionDestinatario();
        if (ptA > ptB) return d.getNombreRemitente();
        if (ptB > ptA) return d.getNombreDestinatario();
        return null;
    }
}
