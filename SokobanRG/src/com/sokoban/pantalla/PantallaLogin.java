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
import com.sokoban.SokobanJuego;
import com.sokoban.model.Usuario;

public class PantallaLogin extends PantallaBase {
    private TextField campoUsuario;
    private TextField campoContrasenia;
    private Label etiquetaMensaje;

    public PantallaLogin(SokobanJuego juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();

   
        juegoSokoban.getManejadorRecursos().reproducirMusicaMenu(60);

        if (juegoSokoban.getManejadorRecursos().getSkinUI() == null) {
            System.out.println("Manejador de Recursos: Skin no cargada en PantallaLogin. Modo texto activo.");
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

        String idioma = "es";
        String textoIniciar = juegoSokoban.getManejadorRecursos().obtenerTexto("titulo_login", idioma);
        Label subtitulo = new Label(textoIniciar, skin, "subtitle");

        campoUsuario     = crearCampoTexto("  " + juegoSokoban.getManejadorRecursos().obtenerTexto("usuario", idioma));
        
        Table tablaContrasenia = new Table();
        campoContrasenia = crearCampoTexto("  " + juegoSokoban.getManejadorRecursos().obtenerTexto("contrasena", idioma));
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

        etiquetaMensaje = new Label("", skin, "message");

        TextButton botonLogin       = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("ingresar", idioma));
        TextButton botonIrARegistro = new TextButton(juegoSokoban.getManejadorRecursos().obtenerTexto("crear_cuenta", idioma), skin, "secondary");

        tablaPanel.add(titulo).spaceBottom(2).row();
        tablaPanel.add(subtitulo).spaceBottom(28).row();
        tablaPanel.add(campoUsuario).width(310).height(46).spaceBottom(14).row();
        tablaPanel.add(tablaContrasenia).width(318).height(46).spaceBottom(10).row();
        tablaPanel.add(etiquetaMensaje).height(24).spaceBottom(16).row();
        tablaPanel.add(botonLogin).width(240).height(48).spaceBottom(12).row();
        tablaPanel.add(botonIrARegistro).width(240).height(42).row();

        tablaExterior.add(tablaPanel).pad(3).center();

        botonLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String rawUsuario = campoUsuario.getText();
                String rawContrasenia = campoContrasenia.getText();

                if (rawContrasenia.length() > 0 && rawContrasenia.charAt(0) == ' ') {
                    actualizarMensaje("La contrasena no puede iniciar con espacio.");
                    return;
                }

                String usuarioLimpio = rawUsuario.trim();

                String contraseniaLimpia = rawContrasenia.stripTrailing();

                autenticarUsuario(usuarioLimpio, contraseniaLimpia);
            }
        });

        botonIrARegistro.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaRegistro();
            }
        });
    }

    private void autenticarUsuario(String nombreUsuario, String contrasenia) {
        if (nombreUsuario.isEmpty() || contrasenia.isEmpty()) {
            actualizarMensaje("Por favor, llene todos los campos.");
            return;
        }

        Usuario u = juegoSokoban.getAlmacenamiento().cargarUsuario(nombreUsuario);
        if (u != null) {
            if (u.getContrasenia().equals(contrasenia)) {
                if (!u.isCuentaActiva()) {
                    if (etiquetaMensaje.getText().toString().contains("habilitarla")) {
                        u.setCuentaActiva(true);
                        juegoSokoban.getAlmacenamiento().guardarUsuario(u);
                        actualizarMensaje("Cuenta reactivada. Presione INGRESAR nuevamente.");
                        return;
                    } else {
                        actualizarMensaje("Cuenta deshabilitada. Presione INGRESAR para habilitarla.");
                        return;
                    }
                }
                
                u.setUltimaSesion(new java.util.Date());
                juegoSokoban.getAlmacenamiento().guardarUsuario(u);
                
                juegoSokoban.setUsuarioActual(u);
                juegoSokoban.getManejadorRecursos().reproducirMusicaMenu(u.getVolumen());
                
                actualizarMensaje("¡Bienvenido, " + u.getNombreCompleto() + "!");
                juegoSokoban.mostrarPantallaMenu();
                return;
            } else {
                actualizarMensaje("Contraseña incorrecta.");
                return;
            }
        }
        actualizarMensaje("El usuario no existe.");
    }

    private void actualizarMensaje(String texto) {
        if (etiquetaMensaje != null) {
            etiquetaMensaje.setText(texto);
        } else {
            System.out.println("Mensaje de Login: " + texto);
        }
    }
}
