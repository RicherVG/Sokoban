package com.sokoban.pantalla;

/**
 * @author riche
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sokoban.SokobanJuego;
import com.sokoban.model.Usuario;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PantallaEstadisticas extends PantallaBase {

    public PantallaEstadisticas(SokobanJuego juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();

        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();
        if (skin == null) return;

        Usuario usuario = juegoSokoban.getUsuarioActual();
        String idioma = (usuario != null) ? usuario.getIdioma() : "es";

        Table tablaExterior = new Table();
        tablaExterior.setFillParent(true);
        escenario.addActor(tablaExterior);

        Table tablaPanel = new Table();
        tablaPanel.setBackground(skin.getDrawable("panel-bg"));
        tablaPanel.pad(20, 30, 20, 30);

        Label titulo = new Label("Perfil y Estadisticas", skin, "title");

        Table tablaMisStats = new Table();
        if (usuario != null) {
            String txtNivel      = juegoSokoban.getManejadorRecursos().obtenerTexto("nivel_max", idioma);
            String txtPartidas   = juegoSokoban.getManejadorRecursos().obtenerTexto("partidas", idioma);
            String txtTiempo     = juegoSokoban.getManejadorRecursos().obtenerTexto("tiempo_total", idioma);
            String txtPuntuacion = juegoSokoban.getManejadorRecursos().obtenerTexto("puntuacion", idioma);
            String txtCompletados = idioma.equals("en") ? "Levels completed: " : "Niveles completados: ";
            String txtPromedio    = idioma.equals("en") ? "Avg time/level: " : "Tiempo prom/nivel: ";
            String txtModoActual  = usuario.isModoSinVidas()
                    ? (idioma.equals("en") ? "Mode: No Lives (no score)" : "Modo: Sin Vidas (sin puntos)")
                    : (idioma.equals("en") ? "Mode: Normal" : "Modo: Normal");

            tablaMisStats.add(new Label(txtNivel    + " " + usuario.getNivelMaximoDesbloqueado(), skin, "default")).left().padBottom(4).row();
            tablaMisStats.add(new Label(txtPartidas + " " + usuario.getPartidasJugadas(),          skin, "default")).left().padBottom(4).row();
            tablaMisStats.add(new Label(txtCompletados + usuario.getNivelesCompletados(),           skin, "default")).left().padBottom(4).row();
            tablaMisStats.add(new Label(txtTiempo   + " " + usuario.getTiempoTotalJugado() + "s",  skin, "default")).left().padBottom(4).row();
            tablaMisStats.add(new Label(txtPromedio + usuario.getTiempoPromedioPorNivel() + "s",   skin, "default")).left().padBottom(4).row();

            Label lblPts = new Label(txtPuntuacion + " " + usuario.getPuntuacionGeneral() + " pts", skin, "default");
            lblPts.setColor(1f, 0.85f, 0.1f, 1f);
            tablaMisStats.add(lblPts).left().padBottom(4).row();

            Label lblModo = new Label(txtModoActual, skin, "default");
            lblModo.setColor(0.6f, 0.85f, 1f, 1f);
            tablaMisStats.add(lblModo).left().padBottom(4).row();

            Map<Integer, Integer> mejores = usuario.getMejorPuntuacionPorNivel();
            if (!mejores.isEmpty()) {
                Table tablaMejores = new Table();
                for (int n = 1; n <= 5; n++) {
                    int pts = mejores.getOrDefault(n, 0);
                    Label lbl = new Label("N" + n + ":" + pts + "p ", skin, "default");
                    if (pts > 0) lbl.setColor(1f, 0.85f, 0.1f, 1f);
                    tablaMejores.add(lbl).padRight(8);
                }
                tablaMisStats.add(tablaMejores).left().padTop(4).row();
            }
        }

        // Profile Card
        Table tablaPerfil = new Table();
        tablaPerfil.setBackground(skin.getDrawable("border-bg"));
        tablaPerfil.pad(15);

        Table tablaAvatarCol = new Table();
        Image imgPerfil = null;
        String rutaP = (usuario != null) ? usuario.getRutaAvatar() : "default.png";
        if (rutaP == null || rutaP.trim().isEmpty()) rutaP = "default.png";
        try {
            Texture tex = null;
            if (rutaP.equals("default.png") || rutaP.contains("default")) {
                if (Gdx.files.internal("assets/avatares/default.png").exists())
                    tex = new Texture(Gdx.files.internal("assets/avatares/default.png"));
            } else {
                File f = new File(rutaP);
                if (f.exists()) tex = new Texture(Gdx.files.absolute(rutaP));
            }
            if (tex != null) imgPerfil = new Image(tex);
        } catch (Exception e) {}

        if (imgPerfil != null) {
            tablaAvatarCol.add(imgPerfil).width(96).height(96).row();
        } else {
            tablaAvatarCol.add().width(96).height(96).row();
        }

        Table tablaInfoCol = new Table();
        tablaInfoCol.top().left();
        if (usuario != null) {
            SimpleDateFormat sdfReg = new SimpleDateFormat("dd/MM/yyyy");
            String fechaRegStr = usuario.getFechaRegistro() != null ? sdfReg.format(usuario.getFechaRegistro()) : "-";

            Label lblNombre = new Label(usuario.getNombreCompleto(), skin, "subtitle");
            Label lblUser = new Label("@" + usuario.getNombreUsuario() + " | Reg: " + fechaRegStr, skin, "default");
            lblUser.setColor(0.7f, 0.75f, 0.9f, 1f);

            tablaInfoCol.add(lblNombre).left().row();
            tablaInfoCol.add(lblUser).left().padBottom(8).row();
            tablaInfoCol.add(tablaMisStats).left().row();
        }

        tablaPerfil.add(tablaAvatarCol).padRight(20).top();
        tablaPerfil.add(tablaInfoCol).expand().fill().top();

        Label lblRankingTitulo = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("ranking", idioma), skin, "subtitle");

        Table tablaAmigos = new Table();
        com.badlogic.gdx.scenes.scene2d.ui.TextField txtAmigo = new com.badlogic.gdx.scenes.scene2d.ui.TextField("", skin);
        txtAmigo.setMessageText("Usuario amigo...");
        TextButton btnAgregarAmigo = new TextButton("Agregar Amigo", skin, "secondary");
        Label lblMensajeAmigo = new Label("", skin, "default");
        lblMensajeAmigo.setColor(Color.RED);
        
        TextButton btnToggleRanking = new TextButton("Ver Amigos", skin, "secondary");
        final boolean[] viendoAmigos = {false};

        tablaAmigos.add(txtAmigo).width(150).padRight(10);
        tablaAmigos.add(btnAgregarAmigo).width(120).padRight(10);
        tablaAmigos.add(btnToggleRanking).width(120).row();
        tablaAmigos.add(lblMensajeAmigo).colspan(3).left().padTop(5);

        Table tablaRanking = new Table();
        Runnable actualizarRanking = new Runnable() {
            @Override
            public void run() {
                tablaRanking.clear();
                List<Usuario> todos = juegoSokoban.getAlmacenamiento().cargarUsuarios();
                List<Usuario> activos = new java.util.ArrayList<>();
                for (Usuario u : todos) {
                    if (u.isCuentaActiva()) {
                        activos.add(u);
                    }
                }
                todos = activos;

                if (viendoAmigos[0] && usuario != null) {
                    List<Usuario> filtrados = new java.util.ArrayList<>();
                    for (Usuario u : todos) {
                        if (u.getNombreUsuario().equals(usuario.getNombreUsuario()) || 
                            usuario.getAmigos().contains(u.getNombreUsuario())) {
                            filtrados.add(u);
                        }
                    }
                    todos = filtrados;
                }

                Collections.sort(todos, new Comparator<Usuario>() {
                    @Override
                    public int compare(Usuario u1, Usuario u2) {
                        return Integer.compare(u2.getPuntuacionGeneral(), u1.getPuntuacionGeneral());
                    }
                });

                Color[] coloresMedalla = {
                    new Color(1f, 0.85f, 0.1f, 1f),
                    new Color(0.75f, 0.75f, 0.82f, 1f),
                    new Color(0.8f, 0.5f, 0.3f, 1f)
                };
                String miNombreU = (usuario != null) ? usuario.getNombreUsuario() : "";
                String txtProm = idioma.equals("en") ? "avg" : "prom";
                String txtLvls = idioma.equals("en") ? "lvls" : "niv";
                tablaRanking.top().left();

                for (int i = 0; i < todos.size(); i++) {
                    Usuario u = todos.get(i);

                    Image imgAvatar = null;
                    String ruta = u.getRutaAvatar();
                    if (ruta == null || ruta.trim().isEmpty()) ruta = "default.png";
                    try {
                        Texture tex = null;
                        if (ruta.equals("default.png") || ruta.contains("default")) {
                            if (Gdx.files.internal("assets/avatares/default.png").exists())
                                tex = new Texture(Gdx.files.internal("assets/avatares/default.png"));
                        } else {
                            File f = new File(ruta);
                            if (f.exists()) tex = new Texture(Gdx.files.absolute(ruta));
                        }
                        if (tex != null) imgAvatar = new Image(tex);
                    } catch (Exception e) {
                        // ignore
                    }

                    String posStr = (i + 1) + ".";
                    String nombreDisplay = u.getNombreUsuario() + (u.getNombreUsuario().equals(miNombreU) ? " (Tu)" : "");
                    String infoExtra = u.getPuntuacionGeneral() + " pts | "
                            + u.getTiempoPromedioPorNivel() + "s " + txtProm + " | "
                            + u.getNivelesCompletados() + " " + txtLvls;

                    Label lblPos     = new Label(posStr, skin, "default");
                    Label lblNomFila = new Label(nombreDisplay, skin, "default");
                    Label lblInfo    = new Label(infoExtra, skin, "default");
                    lblInfo.setColor(0.7f, 0.75f, 0.9f, 1f);

                    if (i < 3) {
                        lblPos.setColor(coloresMedalla[i]);
                        lblNomFila.setColor(coloresMedalla[i]);
                    }
                    if (u.getNombreUsuario().equals(miNombreU)) {
                        lblNomFila.setColor(0.4f, 0.9f, 0.5f, 1f);
                    }

                    if (imgAvatar != null) {
                        tablaRanking.add(imgAvatar).width(34).height(34).padRight(8).padBottom(6);
                    } else {
                        tablaRanking.add().width(34).height(34).padRight(8).padBottom(6);
                    }
                    tablaRanking.add(lblPos).width(28).padRight(6).padBottom(6);
                    tablaRanking.add(lblNomFila).width(130).left().padRight(12).padBottom(6);
                    tablaRanking.add(lblInfo).left().expandX().padBottom(6).row();
                }
            }
        };

        actualizarRanking.run();

        btnToggleRanking.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                viendoAmigos[0] = !viendoAmigos[0];
                btnToggleRanking.setText(viendoAmigos[0] ? "Ver Global" : "Ver Amigos");
                lblRankingTitulo.setText(viendoAmigos[0] ? "Ranking de Amigos" : juegoSokoban.getManejadorRecursos().obtenerTexto("ranking", idioma));
                actualizarRanking.run();
            }
        });

        btnAgregarAmigo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (usuario == null) return;
                String nomAmigo = txtAmigo.getText().trim();
                if (nomAmigo.isEmpty()) {
                    lblMensajeAmigo.setText("Ingresa un nombre");
                    lblMensajeAmigo.setColor(Color.RED);
                    return;
                }
                if (nomAmigo.equals(usuario.getNombreUsuario())) {
                    lblMensajeAmigo.setText("No puedes agregarte a ti mismo");
                    lblMensajeAmigo.setColor(Color.RED);
                    return;
                }
                if (usuario.getAmigos().contains(nomAmigo)) {
                    lblMensajeAmigo.setText("Ya es tu amigo");
                    lblMensajeAmigo.setColor(Color.RED);
                    return;
                }
                
                List<Usuario> todos = juegoSokoban.getAlmacenamiento().cargarUsuarios();
                boolean existe = false;
                Usuario userAmigo = null;
                for (Usuario u : todos) {
                    if (u.getNombreUsuario().equals(nomAmigo) && u.isCuentaActiva()) {
                        existe = true;
                        userAmigo = u;
                        break;
                    }
                }
                if (!existe) {
                    lblMensajeAmigo.setText("Usuario no encontrado o inactivo");
                    lblMensajeAmigo.setColor(Color.RED);
                    return;
                }
                
                if (userAmigo.getSolicitudesAmistad().contains(usuario.getNombreUsuario())) {
                    lblMensajeAmigo.setText("Solicitud ya enviada");
                    lblMensajeAmigo.setColor(Color.RED);
                    return;
                }
                
                userAmigo.agregarSolicitudAmistad(usuario.getNombreUsuario());
                juegoSokoban.getAlmacenamiento().guardarUsuario(userAmigo);
                lblMensajeAmigo.setText("Solicitud enviada a " + nomAmigo);
                lblMensajeAmigo.setColor(Color.GREEN);
                txtAmigo.setText("");
                if (viendoAmigos[0]) {
                    actualizarRanking.run();
                }
            }
        });

        com.badlogic.gdx.scenes.scene2d.ui.ScrollPane scrollRanking = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(tablaRanking, skin);
        scrollRanking.setScrollingDisabled(true, false);
        scrollRanking.setFadeScrollBars(false);

        TextButton btnVolver = new TextButton(juegoSokoban.getManejadorRecursos().obtenerTexto("volver", idioma), skin, "secondary");
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaMenu();
            }
        });

        final Table tablaSolicitudes = new Table();
        final Runnable actualizarSolicitudes = new Runnable() {
            @Override
            public void run() {
                tablaSolicitudes.clear();
                if (usuario == null) return;
                List<String> pendientes = usuario.getSolicitudesAmistad();
                if (pendientes.isEmpty()) return;
                
                tablaSolicitudes.add(new Label("Solicitudes de Amistad Pendientes:", skin, "subtitle")).left().colspan(3).padBottom(5).row();
                for (int i = 0; i < pendientes.size(); i++) {
                    final String req = pendientes.get(i);
                    Label lblReq = new Label(req, skin, "default");
                    TextButton btnAceptar = new TextButton("Aceptar", skin, "secondary");
                    btnAceptar.getLabel().setColor(Color.GREEN);
                    TextButton btnRechazar = new TextButton("Rechazar", skin, "secondary");
                    btnRechazar.getLabel().setColor(Color.RED);
                    
                    btnAceptar.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            usuario.agregarAmigo(req);
                            usuario.removerSolicitudAmistad(req);
                            Usuario otro = juegoSokoban.getAlmacenamiento().cargarUsuario(req);
                            if (otro != null) {
                                otro.agregarAmigo(usuario.getNombreUsuario());
                                juegoSokoban.getAlmacenamiento().guardarUsuario(otro);
                            }
                            juegoSokoban.getAlmacenamiento().guardarUsuario(usuario);
                            actualizarRanking.run();
                            run(); // actualiza esta tabla
                        }
                    });
                    
                    btnRechazar.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            usuario.removerSolicitudAmistad(req);
                            juegoSokoban.getAlmacenamiento().guardarUsuario(usuario);
                            run();
                        }
                    });
                    
                    tablaSolicitudes.add(lblReq).width(120).left();
                    tablaSolicitudes.add(btnAceptar).width(90).padRight(10);
                    tablaSolicitudes.add(btnRechazar).width(90).row();
                }
            }
        };
        actualizarSolicitudes.run();

        tablaPanel.add(titulo).spaceBottom(16).row();
        tablaPanel.add(tablaPerfil).width(520).spaceBottom(10).row();
        tablaPanel.add(tablaSolicitudes).left().spaceBottom(10).row();
        
        Table tablaTopRanking = new Table();
        tablaTopRanking.add(lblRankingTitulo).left().expandX();
        tablaPanel.add(tablaTopRanking).fillX().spaceBottom(8).row();
        
        tablaPanel.add(tablaAmigos).left().spaceBottom(12).row();
        tablaPanel.add(scrollRanking).width(520).height(180).left().spaceBottom(24).row();
        tablaPanel.add(btnVolver).width(250).height(42).row();

        com.badlogic.gdx.scenes.scene2d.ui.ScrollPane mainScroll = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(tablaPanel, skin);
        mainScroll.setFadeScrollBars(false);
        mainScroll.setScrollingDisabled(true, false);
        tablaExterior.add(mainScroll).expand().fill();
    }
}
