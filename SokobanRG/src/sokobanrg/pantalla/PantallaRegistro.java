package sokobanrg.pantalla;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import sokobanrg.SokobanJuego;
import sokobanrg.model.Usuario;
import java.util.List;

public class PantallaRegistro extends PantallaBase {
    private TextField campoNombreCompleto;
    private TextField campoUsuario;
    private TextField campoContrasenia;
    private Label etiquetaMensaje;

    public PantallaRegistro(SokobanJuego juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();

        if (juegoSokoban.getManejadorRecursos().getSkinUI() == null) {
            System.out.println("Manejador de Recursos: Skin no cargada en PantallaRegistro. Modo texto activo.");
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

        Table lineaSup = new Table();
        lineaSup.setBackground(skin.getDrawable("border-bg"));
        
        Label titulo = new Label("SOKOBAN", skin, "title");
        Label subtitulo = new Label("— Crear Cuenta —", skin, "subtitle");

        campoNombreCompleto = crearCampoTexto("  Nombre completo");
        campoUsuario        = crearCampoTexto("  Nombre de usuario");
        campoContrasenia    = crearCampoTexto("  Contraseña");
        campoContrasenia.setPasswordMode(true);
        campoContrasenia.setPasswordCharacter('*');

        etiquetaMensaje = new Label("", skin, "message");

        TextButton botonRegistrar = crearBoton("▶  REGISTRARSE");
        TextButton botonVolver    = new TextButton("Volver al Login", skin, "secondary");

        Table lineaInf = new Table();
        lineaInf.setBackground(skin.getDrawable("border-bg"));

        tablaPanel.add(lineaSup).width(60).height(4).spaceBottom(15).row();
        tablaPanel.add(titulo).spaceBottom(2).row();
        tablaPanel.add(subtitulo).spaceBottom(28).row();
        
        tablaPanel.add(campoNombreCompleto).width(310).height(46).spaceBottom(10).row();
        tablaPanel.add(campoUsuario).width(310).height(46).spaceBottom(10).row();
        tablaPanel.add(campoContrasenia).width(310).height(46).spaceBottom(10).row();
        
        tablaPanel.add(etiquetaMensaje).height(24).spaceBottom(16).row();
        
        tablaPanel.add(botonRegistrar).width(240).height(48).spaceBottom(12).row();
        tablaPanel.add(botonVolver).width(240).height(42).spaceBottom(25).row();
        
        tablaPanel.add(lineaInf).width(120).height(2).row();

        tablaBorde.add(tablaPanel).pad(2);
        tablaExterior.add(tablaBorde);

        botonRegistrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                registrarUsuario(
                    campoUsuario.getText(),
                    campoContrasenia.getText(),
                    campoNombreCompleto.getText()
                );
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
        if (usuario.isEmpty() || contrasenia.isEmpty() || nombre.isEmpty()) {
            actualizarMensaje("Por favor, llene todos los campos.");
            return;
        }

        if (contrasenia.length() < 5) {
            actualizarMensaje("La contraseña debe tener al menos 5 caracteres.");
            return;
        }

        List<Usuario> usuarios = juegoSokoban.getAlmacenamiento().cargarUsuarios();
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equalsIgnoreCase(usuario)) {
                actualizarMensaje("El nombre de usuario ya está registrado.");
                return;
            }
        }

        Usuario nuevoUsuario = new Usuario(usuario, contrasenia, nombre, "");
        usuarios.add(nuevoUsuario);
        juegoSokoban.getAlmacenamiento().guardarUsuarios(usuarios);

        actualizarMensaje("¡Registro exitoso!");
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
