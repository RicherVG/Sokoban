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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.sokoban.SokobanJuego;
import com.sokoban.model.Usuario;
import com.sokoban.service.HiloNotificacionDesafio;
import java.io.File;

public class PantallaMenu extends PantallaBase {
    private Label lblBienvenida;
    private TextButton btnDesafios;
    private HiloNotificacionDesafio hiloNotificacion;

    public PantallaMenu(SokobanJuego juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();

        Usuario usuario = juegoSokoban.getUsuarioActual();
        String idioma = (usuario != null) ? usuario.getIdioma() : "es";

        int volumen = (usuario != null) ? usuario.getVolumen() : 100;
        juegoSokoban.getManejadorRecursos().reproducirMusicaMenu(volumen);

        if (juegoSokoban.getManejadorRecursos().getSkinUI() == null) {
            return;
        }

        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();

        Table tablaRaiz = new Table();
        tablaRaiz.setFillParent(true);
        escenario.addActor(tablaRaiz);

        Table tablaPanel = new Table();
        tablaPanel.setBackground(skin.getDrawable("panel-bg"));
        tablaPanel.pad(30, 40, 30, 40);

        lblBienvenida = crearEtiqueta(juegoSokoban.getManejadorRecursos().obtenerTexto("bienvenido", idioma), true);
        actualizarDatosUsuario();

        TextButton btnJugar = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("jugar", idioma));
        TextButton btnEstadisticas = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("estadisticas", idioma));
        TextButton btnPreferencias = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("preferencias", idioma));
        btnDesafios = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("desafios", idioma));
        TextButton btnCerrarSesion = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("cerrar_sesion", idioma));
        TextButton btnSalir = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("salir", idioma));

        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaSeleccionNivel();
            }
        });

        btnEstadisticas.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaEstadisticas();
            }
        });

        btnPreferencias.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaPreferencias();
            }
        });

        btnCerrarSesion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                detenerHiloNotificacion();
                juegoSokoban.setUsuarioActual(null);
                juegoSokoban.mostrarPantallaLogin();
            }
        });

        btnDesafios.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                detenerHiloNotificacion();
                juegoSokoban.mostrarPantallaDesafios();
            }
        });

        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Image imgAvatar = null;
        if (usuario != null) {
            String ruta = usuario.getRutaAvatar();
            if (ruta == null || ruta.trim().isEmpty()) ruta = "default.png";

            try {
                Texture tex = null;
                if (ruta.equals("default.png") || ruta.equals("avatares/default.png") || ruta.equals("assets/avatares/default.png")) {
                    if (Gdx.files.internal("assets/avatares/default.png").exists()) {
                        tex = new Texture(Gdx.files.internal("assets/avatares/default.png"));
                    }
                } else {
                    File f = new File(ruta);
                    if (f.exists()) {
                        tex = new Texture(Gdx.files.absolute(ruta));
                    }
                }

                if (tex != null) {
                    imgAvatar = new Image(tex);
                }
            } catch (Exception e) {
                
            }
        }

        if (imgAvatar != null) {
            tablaPanel.add(imgAvatar).width(150).height(150).padBottom(20).row();
        }

        tablaPanel.add(lblBienvenida).padBottom(40).row();
        tablaPanel.add(btnJugar).width(260).height(44).padBottom(10).row();
        tablaPanel.add(btnEstadisticas).width(260).height(44).padBottom(10).row();
        tablaPanel.add(btnDesafios).width(260).height(44).padBottom(10).row();
        tablaPanel.add(btnPreferencias).width(260).height(44).padBottom(10).row();
        tablaPanel.add(btnCerrarSesion).width(260).height(44).padBottom(10).row();
        tablaPanel.add(btnSalir).width(260).height(44);

        tablaRaiz.add(tablaPanel).pad(3);

        if (usuario != null) {
            iniciarHiloNotificacion(usuario.getNombreUsuario(), idioma);
        }
    }

    public void actualizarDatosUsuario() {
        Usuario usuario = juegoSokoban.getUsuarioActual();
        if (usuario != null && lblBienvenida != null) {
            String txtBienvenido = juegoSokoban.getManejadorRecursos().obtenerTexto("bienvenido", usuario.getIdioma());
            lblBienvenida.setText(txtBienvenido + ", " + usuario.getNombreCompleto());
        }
    }

    private void iniciarHiloNotificacion(String nombreUsuario, String idioma) {
        detenerHiloNotificacion();
        final String txtDesafios = juegoSokoban.getManejadorRecursos().obtenerTexto("desafios", idioma);
        final String txtNotif = juegoSokoban.getManejadorRecursos().obtenerTexto("notif_desafio", idioma);
        hiloNotificacion = new HiloNotificacionDesafio(nombreUsuario, juegoSokoban.getAlmacenamiento(), new Runnable() {
            @Override
            public void run() {
                if (btnDesafios == null) return;
                int count = hiloNotificacion != null ? hiloNotificacion.contarNotificaciones() : 0;
                if (count > 0) {
                    btnDesafios.setText(txtDesafios + " (!)");
                    btnDesafios.setColor(com.badlogic.gdx.graphics.Color.SCARLET);
                } else {
                    btnDesafios.setText(txtDesafios);
                    btnDesafios.setColor(com.badlogic.gdx.graphics.Color.WHITE);
                }
            }
        });
        hiloNotificacion.start();
    }

    private void detenerHiloNotificacion() {
        if (hiloNotificacion != null) {
            hiloNotificacion.detener();
            hiloNotificacion = null;
        }
    }

    @Override
    public void hide() {
        super.hide();
        detenerHiloNotificacion();
    }

    @Override
    public void dispose() {
        detenerHiloNotificacion();
        super.dispose();
    }
}
