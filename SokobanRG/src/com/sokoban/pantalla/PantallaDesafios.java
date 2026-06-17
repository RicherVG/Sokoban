package com.sokoban.pantalla;

/**
 * @author riche
 */

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sokoban.SokobanJuego;
import com.sokoban.model.Desafio;
import com.sokoban.model.EstadoDesafio;
import com.sokoban.model.Usuario;
import java.text.SimpleDateFormat;
import java.util.List;

public class PantallaDesafios extends PantallaBase {

    public PantallaDesafios(SokobanJuego juego) {
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
        String yo = usuario.getNombreUsuario();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");

        Table tablaRaiz = new Table();
        tablaRaiz.setFillParent(true);
        escenario.addActor(tablaRaiz);

        Table tablaPanel = new Table();
        tablaPanel.setBackground(skin.getDrawable("panel-bg"));
        tablaPanel.pad(16, 24, 16, 24);

        Label titulo = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("desafios", idioma), skin, "title");
        tablaPanel.add(titulo).padBottom(16).row();

        List<Desafio> desafios = juegoSokoban.getAlmacenamiento().cargarDesafiosDeUsuario(yo);

        Table tablaRecibidos = new Table();
        tablaRecibidos.top().left();

        Table tablaEnviados = new Table();
        tablaEnviados.top().left();

        boolean tieneRecibidos = false;
        boolean tieneEnviados = false;

        for (Desafio d : desafios) {
            if (d.getNombreDestinatario().equals(yo)) {
                tablaRecibidos.add(crearFilaDesafio(d, yo, idioma, skin, sdf)).width(430).padBottom(8).row();
                tieneRecibidos = true;
            }
        }
        for (Desafio d : desafios) {
            if (d.getNombreRemitente().equals(yo)) {
                tablaEnviados.add(crearFilaDesafio(d, yo, idioma, skin, sdf)).width(430).padBottom(8).row();
                tieneEnviados = true;
            }
        }

        if (!tieneRecibidos) {
            tablaRecibidos.add(new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("sin_desafios", idioma), skin, "default")).pad(12);
        }
        if (!tieneEnviados) {
            tablaEnviados.add(new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("sin_desafios", idioma), skin, "default")).pad(12);
        }

        Table columnas = new Table();

        Table panelRecibidos = new Table();
        panelRecibidos.setBackground(skin.getDrawable("panel-bg"));
        panelRecibidos.pad(10);
        Label lblTitRec = new Label("- " + juegoSokoban.getManejadorRecursos().obtenerTexto("recibidos", idioma) + " -", skin, "subtitle");
        ScrollPane scrollRec = new ScrollPane(tablaRecibidos, skin);
        scrollRec.setScrollingDisabled(true, false);
        scrollRec.setFadeScrollBars(false);
        panelRecibidos.add(lblTitRec).padBottom(8).row();
        panelRecibidos.add(scrollRec).width(450).height(250).row();

        Table panelEnviados = new Table();
        panelEnviados.setBackground(skin.getDrawable("panel-bg"));
        panelEnviados.pad(10);
        Label lblTitEnv = new Label("- " + juegoSokoban.getManejadorRecursos().obtenerTexto("enviados", idioma) + " -", skin, "subtitle");
        ScrollPane scrollEnv = new ScrollPane(tablaEnviados, skin);
        scrollEnv.setScrollingDisabled(true, false);
        scrollEnv.setFadeScrollBars(false);
        panelEnviados.add(lblTitEnv).padBottom(8).row();
        panelEnviados.add(scrollEnv).width(450).height(250).row();

        columnas.add(panelRecibidos).width(480).padRight(16);
        columnas.add(panelEnviados).width(480);

        tablaPanel.add(columnas).padBottom(16).row();

        Table botonesInferiores = new Table();
        TextButton btnCrear = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("crear_desafio", idioma));
        TextButton btnVolver = new TextButton(juegoSokoban.getManejadorRecursos().obtenerTexto("volver", idioma), skin, "secondary");

        btnCrear.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaCrearDesafio();
            }
        });

        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaMenu();
            }
        });

        botonesInferiores.add(btnCrear).width(220).height(46).padRight(20);
        botonesInferiores.add(btnVolver).width(180).height(46);
        tablaPanel.add(botonesInferiores).row();

        tablaRaiz.add(tablaPanel).pad(10);
    }

    private Table crearFilaDesafio(final Desafio d, final String yo, final String idioma, Skin skin, SimpleDateFormat sdf) {
        Table fila = new Table();
        fila.setBackground(skin.getDrawable("item-bg"));
        fila.pad(10);

        boolean soyDestinatario = yo.equals(d.getNombreDestinatario());
        String oponente = soyDestinatario ? d.getNombreRemitente() : d.getNombreDestinatario();
        String txtEstado = obtenerTextoEstado(d, yo, idioma);
        String fechaStr = d.getFechaCreacion() != null ? sdf.format(d.getFechaCreacion()) : "-";

        Table infoTable = new Table();
        infoTable.left().top();
        Label lblOponente = new Label("vs " + oponente, skin, "subtitle");
        Label lblDescripcion = new Label(d.getDescripcionNivel() + " | " + fechaStr, skin, "default");
        lblDescripcion.setColor(0.7f, 0.75f, 0.9f, 1f);
        Label lblEstado = new Label(txtEstado, skin, "default");

        infoTable.add(lblOponente).left().row();
        infoTable.add(lblDescripcion).left().row();
        infoTable.add(lblEstado).left().row();

        fila.add(infoTable).expandX().left().padRight(10);

        Table botonesFila = new Table();
        botonesFila.right();

        if (soyDestinatario && d.getEstado() == EstadoDesafio.PENDIENTE) {
            TextButton btnAceptar = new TextButton(idioma.equals("en") ? "Accept" : "Aceptar", skin, "default");
            TextButton btnRechazar = new TextButton(idioma.equals("en") ? "Reject" : "Rechazar", skin, "secondary");

            btnAceptar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Desafio actualizado = juegoSokoban.getAlmacenamiento().cargarDesafio(d.getId());
                    if (actualizado == null) return;
                    actualizado.setEstado(EstadoDesafio.ACEPTADO);
                    juegoSokoban.getAlmacenamiento().guardarDesafio(actualizado);
                    juegoSokoban.iniciarModoDesafio(actualizado);
                    int nivelInicio = actualizado.getNivel() == 0 ? 1 : actualizado.getNivel();
                    juegoSokoban.mostrarPantallaJuego(nivelInicio);
                }
            });

            btnRechazar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Desafio actualizado = juegoSokoban.getAlmacenamiento().cargarDesafio(d.getId());
                    if (actualizado == null) return;
                    actualizado.setEstado(EstadoDesafio.RECHAZADO);
                    juegoSokoban.getAlmacenamiento().guardarDesafio(actualizado);
                    juegoSokoban.mostrarPantallaDesafios();
                }
            });

            botonesFila.add(btnAceptar).width(95).height(32).padBottom(4).row();
            botonesFila.add(btnRechazar).width(95).height(32).row();

        } else if (soyDestinatario && d.getEstado() == EstadoDesafio.ACEPTADO) {
            TextButton btnJugar = new TextButton(idioma.equals("en") ? "Play" : "Jugar", skin, "default");
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
            botonesFila.add(btnJugar).width(95).height(32).row();

        } else if (!soyDestinatario && d.getEstado() == EstadoDesafio.B_JUGO) {
            TextButton btnJugar = new TextButton(idioma.equals("en") ? "Play" : "Jugar", skin, "default");
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
            botonesFila.add(btnJugar).width(95).height(32).row();

        } else if (d.getEstado() == EstadoDesafio.COMPLETADO) {
            TextButton btnVer = new TextButton(idioma.equals("en") ? "Result" : "Ver", skin, "secondary");
            btnVer.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    juegoSokoban.mostrarPantallaResultadoDesafio(d.getId());
                }
            });
            botonesFila.add(btnVer).width(95).height(32).row();
        }

        fila.add(botonesFila).right();
        return fila;
    }

    private String obtenerTextoEstado(Desafio d, String yo, String idioma) {
        switch (d.getEstado()) {
            case PENDIENTE:
                return "Pendiente de aceptacion";
            case ACEPTADO:
                return yo.equals(d.getNombreDestinatario())
                    ? juegoSokoban.getManejadorRecursos().obtenerTexto("jugar_desafio", idioma)
                    : juegoSokoban.getManejadorRecursos().obtenerTexto("esperar_oponente", idioma);
            case B_JUGO:
                return yo.equals(d.getNombreRemitente())
                    ? juegoSokoban.getManejadorRecursos().obtenerTexto("tu_turno", idioma)
                    : juegoSokoban.getManejadorRecursos().obtenerTexto("esperar_oponente", idioma);
            case COMPLETADO:
                return "Terminado";
            case RECHAZADO:
                return "Rechazado";
            default:
                return "";
        }
    }
}
