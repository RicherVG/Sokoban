package com.sokoban.pantalla;
/**
 *
 * @author riche
 */

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.sokoban.SokobanJuego;
import com.sokoban.model.Usuario;
import javax.swing.JFileChooser;
import java.io.File;

public class PantallaPreferencias extends PantallaBase {
    private int volumenActual;
    private String idiomaActual;
    private String rutaAvatarActual;
    private boolean modoSinVidasActual;
    private Label lblVolumen;
    private Label lblFotoActual;


    public PantallaPreferencias(SokobanJuego juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();

        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();
        if (skin == null) return;

        Usuario usuario = juegoSokoban.getUsuarioActual();
        if (usuario != null) {
            volumenActual = usuario.getVolumen();
            idiomaActual = usuario.getIdioma();
            rutaAvatarActual = usuario.getRutaAvatar();
            modoSinVidasActual = usuario.isModoSinVidas();
            if (rutaAvatarActual == null || rutaAvatarActual.trim().isEmpty()) {
                rutaAvatarActual = "default.png";
            }
        } else {
            volumenActual = 100;
            idiomaActual = "es";
            rutaAvatarActual = "default.png";
            modoSinVidasActual = false;
        }

        Table tablaExterior = new Table();
        tablaExterior.setFillParent(true);
        escenario.addActor(tablaExterior);

        Table tablaPanel = new Table();
        tablaPanel.setBackground(skin.getDrawable("panel-bg"));
        tablaPanel.pad(30, 40, 30, 40);

        Label titulo = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("preferencias", idiomaActual), skin, "title");
        
        Label lblIdiomaDesc = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("idioma", idiomaActual), skin, "subtitle");
        TextButton btnIdioma = crearBoton(idiomaActual.equals("es") ? "Español" : "English");

        Label lblVolumenDesc = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("volumen", idiomaActual), skin, "subtitle");
        lblVolumen = new Label(volumenActual + "%", skin, "default");
        
        TextButton btnVolMas = crearBoton("+");
        TextButton btnVolMenos = crearBoton("-");
        TextButton btnMute = crearBoton("Mute");

        TextButton btnGuardar = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("guardar", idiomaActual));
        TextButton btnVolver = new TextButton(juegoSokoban.getManejadorRecursos().obtenerTexto("volver", idiomaActual), skin, "secondary");

        btnIdioma.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (idiomaActual.equals("es")) {
                    idiomaActual = "en";
                    btnIdioma.setText("English");
                } else {
                    idiomaActual = "es";
                    btnIdioma.setText("Español");
                }
            }
        });

        btnVolMas.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (volumenActual < 100) {
                    volumenActual += 10;
                    if (volumenActual > 100) volumenActual = 100;
                    actualizarVolumen();
                }
            }
        });

        btnVolMenos.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (volumenActual > 0) {
                    volumenActual -= 10;
                    if (volumenActual < 0) volumenActual = 0;
                    actualizarVolumen();
                }
            }
        });

        btnMute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                volumenActual = 0;
                actualizarVolumen();
            }
        });

        // Modo sin vidas
        final TextButton btnSinVidas = new TextButton(
            modoSinVidasActual ? "Modo sin vidas: ON" : "Modo sin vidas: OFF", skin, modoSinVidasActual ? "default" : "secondary");
        btnSinVidas.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                modoSinVidasActual = !modoSinVidasActual;
                btnSinVidas.setText(modoSinVidasActual ? "Modo sin vidas: ON" : "Modo sin vidas: OFF");
            }
        });

        // Avatar
        Label lblAvatar = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("avatar", idiomaActual), skin, "subtitle");
        lblFotoActual = new Label(rutaAvatarActual.length() > 40 ? "..." + rutaAvatarActual.substring(rutaAvatarActual.length() - 40) : rutaAvatarActual, skin, "default");
        lblFotoActual.setColor(0.6f, 0.65f, 0.85f, 1f);
        TextButton btnBuscar = crearBoton("Buscar imagen");
        TextButton btnEliminar = new TextButton("Eliminar foto", skin, "secondary");

        btnEliminar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rutaAvatarActual = "default.png";
                lblFotoActual.setText(rutaAvatarActual);
            }
        });

        btnBuscar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Seleccionar Avatar");
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));
                        int result = fileChooser.showOpenDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            final File selectedFile = fileChooser.getSelectedFile();
                            String name = selectedFile.getName().toLowerCase();
                            if (!name.endsWith(".jpg") && !name.endsWith(".jpeg") && !name.endsWith(".png")) {
                                javax.swing.JOptionPane.showMessageDialog(null, "Formato no válido. Usa JPG o PNG.");
                                return;
                            }
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    rutaAvatarActual = selectedFile.getAbsolutePath();
                                    String corto = rutaAvatarActual.length() > 40 ? "..." + rutaAvatarActual.substring(rutaAvatarActual.length() - 40) : rutaAvatarActual;
                                    lblFotoActual.setText(corto);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        Table tablaAvatar = new Table();
        tablaAvatar.add(lblAvatar).left().padBottom(4).row();
        tablaAvatar.add(lblFotoActual).left().padBottom(6).row();
        Table botonesAvatar = new Table();
        botonesAvatar.add(btnBuscar).width(140).height(40).padRight(10);
        botonesAvatar.add(btnEliminar).width(120).height(40);
        tablaAvatar.add(botonesAvatar).left().row();

        btnGuardar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (usuario != null) {
                    usuario.setVolumen(volumenActual);
                    usuario.setIdioma(idiomaActual);
                    usuario.setModoSinVidas(modoSinVidasActual);
                    if (rutaAvatarActual != null && !rutaAvatarActual.isEmpty()) {
                        usuario.setRutaAvatar(rutaAvatarActual);
                    }
                    juegoSokoban.getAlmacenamiento().guardarUsuario(usuario);
                }
                juegoSokoban.getManejadorRecursos().reproducirMusicaMenu(volumenActual);
                juegoSokoban.mostrarPantallaMenu();
            }
        });

        TextButton btnDesactivar = new TextButton("Desactivar Cuenta", skin, "secondary");
        btnDesactivar.getLabel().setColor(com.badlogic.gdx.graphics.Color.RED);
        btnDesactivar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (usuario != null) {
                    usuario.setCuentaActiva(false);
                    juegoSokoban.getAlmacenamiento().guardarUsuario(usuario);
                    juegoSokoban.setUsuarioActual(null);
                    juegoSokoban.getManejadorRecursos().detenerMusica();
                    juegoSokoban.mostrarPantallaLogin();
                }
            }
        });

        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.getManejadorRecursos().reproducirMusicaMenu(usuario != null ? usuario.getVolumen() : 100);
                juegoSokoban.mostrarPantallaMenu();
            }
        });

        Table tablaVolumen = new Table();
        tablaVolumen.add(btnVolMenos).width(50).height(40).padRight(10);
        tablaVolumen.add(lblVolumen).width(60).padRight(10);
        tablaVolumen.add(btnVolMas).width(50).height(40).padRight(10);
        tablaVolumen.add(btnMute).width(80).height(40);

        tablaPanel.add(titulo).spaceBottom(20).row();
        tablaPanel.add(lblIdiomaDesc).spaceBottom(6).row();
        tablaPanel.add(btnIdioma).width(200).height(40).spaceBottom(16).row();
        tablaPanel.add(lblVolumenDesc).spaceBottom(6).row();
        tablaPanel.add(tablaVolumen).spaceBottom(16).row();
        tablaPanel.add(btnSinVidas).width(260).height(40).spaceBottom(16).row();
        tablaPanel.add(tablaAvatar).spaceBottom(20).row();
        tablaPanel.add(btnGuardar).width(250).height(48).spaceBottom(12).row();
        tablaPanel.add(btnDesactivar).width(250).height(42).spaceBottom(12).row();
        tablaPanel.add(btnVolver).width(250).height(42).row();

        com.badlogic.gdx.scenes.scene2d.ui.ScrollPane mainScroll = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(tablaPanel, skin);
        mainScroll.setFadeScrollBars(false);
        mainScroll.setScrollingDisabled(true, false);
        tablaExterior.add(mainScroll).expand().fill();
    }

    private void actualizarVolumen() {
        lblVolumen.setText(volumenActual + "%");
        juegoSokoban.getManejadorRecursos().reproducirMusicaMenu(volumenActual);
    }
}
