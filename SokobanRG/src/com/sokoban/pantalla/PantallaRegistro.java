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
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

public class PantallaRegistro extends PantallaBase {
    private TextField campoNombreCompleto;
    private TextField campoUsuario;
    private TextField campoContrasenia;
    private TextField campoAvatar;
    private Label etiquetaMensaje;

    public PantallaRegistro(SokobanJuego juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();

        if (juegoSokoban.getManejadorRecursos().getSkinUI() == null) {
            System.out.println("Skin no cargada.");
            return;
        }

        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();

        Table tablaExterior = new Table();
        tablaExterior.setFillParent(true);
        escenario.addActor(tablaExterior);

        Table tablaPanel = new Table();
        tablaPanel.setBackground(skin.getDrawable("panel-bg"));
        tablaPanel.pad(30, 40, 30, 40);

        Label titulo = new Label("SOKOBAN", skin, "title");
        Label subtitulo = new Label("Crear Cuenta", skin, "subtitle");

        campoNombreCompleto = crearCampoTexto("  Nombre completo");
        campoUsuario        = crearCampoTexto("  Nombre de usuario");

        Table tablaContrasenia = new Table();
        campoContrasenia    = crearCampoTexto("  Contrasena");
        campoContrasenia.setPasswordMode(true);
        campoContrasenia.setPasswordCharacter('*');
        TextButton botonVer = new TextButton("Ver", skin, "secondary");
        botonVer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                campoContrasenia.setPasswordMode(!campoContrasenia.isPasswordMode());
                botonVer.setText(campoContrasenia.isPasswordMode() ? "Ver" : "Ocultar");
            }
        });
        tablaContrasenia.add(campoContrasenia).width(230).height(46);
        tablaContrasenia.add(botonVer).width(80).height(46).padLeft(8);


        Table tablaAvatar = new Table();
        campoAvatar = crearCampoTexto("  Avatar (Opcional)");
        campoAvatar.setDisabled(true);
        TextButton botonBuscarAvatar = new TextButton("Buscar", skin, "default");
        botonBuscarAvatar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileDialog dialogo = new FileDialog((Frame) null, "Seleccionar Avatar");
                        dialogo.setMode(FileDialog.LOAD);
                        dialogo.setFilenameFilter(new java.io.FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                String lower = name.toLowerCase();
                                return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
                            }
                        });
                        dialogo.setVisible(true);
                        final String dir  = dialogo.getDirectory();
                        final String file = dialogo.getFile();
                        if (dir != null && file != null) {
                            final String ruta = dir + file;
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    campoAvatar.setText(ruta);
                                }
                            });
                        }
                        dialogo.dispose();
                    }
                }).start();
            }
        });
        tablaAvatar.add(campoAvatar).width(230).height(46);
        tablaAvatar.add(botonBuscarAvatar).width(80).height(46).padLeft(8);

        etiquetaMensaje = new Label("", skin, "message");
        etiquetaMensaje.setWrap(true);

        TextButton botonRegistrar = crearBoton("REGISTRARSE");
        TextButton botonVolver    = new TextButton("Volver al Login", skin, "secondary");

        tablaPanel.add(titulo).spaceBottom(2).row();
        tablaPanel.add(subtitulo).spaceBottom(28).row();

        tablaPanel.add(campoNombreCompleto).width(310).height(46).spaceBottom(10).row();
        tablaPanel.add(campoUsuario).width(310).height(46).spaceBottom(10).row();
        tablaPanel.add(tablaContrasenia).width(310).height(46).spaceBottom(4).row();

        tablaPanel.add(tablaAvatar).width(310).height(46).spaceBottom(10).row();

        tablaPanel.add(etiquetaMensaje).width(310).height(40).spaceBottom(12).row();

        tablaPanel.add(botonRegistrar).width(240).height(48).spaceBottom(12).row();
        tablaPanel.add(botonVolver).width(240).height(42).spaceBottom(25).row();

        tablaExterior.add(tablaPanel);

        botonRegistrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String rawUsuario = campoUsuario.getText();
                String rawContrasenia = campoContrasenia.getText();
                String rawNombre = campoNombreCompleto.getText();

                String nombreLimpio = rawNombre.trim();
                String usuarioLimpio = rawUsuario.trim();

                // Contrasenia: no puede empezar con espacio, trim al final
                if (rawContrasenia.length() > 0 && rawContrasenia.charAt(0) == ' ') {
                    actualizarMensaje("La contrasenia NO puede iniciar con espacio.");
                    return;
                }
                String contraseniaLimpia = rawContrasenia.stripTrailing();

                registrarUsuario(usuarioLimpio, contraseniaLimpia, nombreLimpio);
            }
        });

        botonVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaLogin();
            }
        });
    }

    private void registrarUsuario(String usuario, String contrasenia, String nombre) {
        if (nombre.isEmpty()) {
            actualizarMensaje("Ingresa tu nombre completo.");
            return;
        }
        if (usuario.isEmpty()) {
            actualizarMensaje("Ingresa un nombre de usuario.");
            return;
        }
        if (usuario.contains(" ")) {
            actualizarMensaje("El usuario NO puede contener espacios.");
            return;
        }

        if (contrasenia.isEmpty()) {
            actualizarMensaje("Ingresa una contrasena.");
            return;
        }
        if (contrasenia.length() < 5) {
            actualizarMensaje("La contrasena debe tener al menos 5 caracteres.");
            return;
        }
        if (!contrasenia.matches(".*[A-Z].*")) {
            actualizarMensaje("La contrasenia debe tener al menos una MAYUSCULA.");
            return;
        }
        if (!contrasenia.matches(".*[a-z].*")) {
            actualizarMensaje("La contrasena debe tener al menos una minuscula.");
            return;
        }
        if (!contrasenia.matches(".*[0-9].*")) {
            actualizarMensaje("La contrasena debe tener al menos un NUMERO.");
            return;
        }

        if (juegoSokoban.getAlmacenamiento().existeUsuario(usuario)) {
            actualizarMensaje("El nombre de usuario ya esta en uso.");
            return;
        }

        String rutaAvatar = campoAvatar.getText().trim();
        if (rutaAvatar.isEmpty()) {
            rutaAvatar = "default.png";
        }

        Usuario nuevoUsuario = new Usuario(usuario, contrasenia, nombre, rutaAvatar);
        juegoSokoban.getAlmacenamiento().guardarUsuario(nuevoUsuario);

        actualizarMensaje("Registro exitoso!");
        juegoSokoban.mostrarPantallaLogin();
    }

    private void actualizarMensaje(String texto) {
        if (etiquetaMensaje != null) {
            etiquetaMensaje.setText(texto);
        } else {
            System.out.println("Mensaje de Registro: " + texto);
        }
    }
}
