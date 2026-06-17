package com.sokoban.service;
/**
 *
 * @author riche
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.util.HashMap;
import java.util.Map;

public class ManejadorRecursos {
    private BitmapFont fuentePrincipal;
    private BitmapFont fuenteTitulo;
    private Skin skinUI;
    
    private Music musicaMenu;
    private Music musicaJuego;
    
 
    private Map<String, String> textosEspanol;
    private Map<String, String> textosIngles;

    public ManejadorRecursos() {
        textosEspanol = new HashMap<>();
        textosIngles = new HashMap<>();
        cargarTraducciones();
    }

    private void cargarTraducciones() {
        textosEspanol.put("titulo_login", "Iniciar Sesión");
        textosEspanol.put("usuario", "Nombre de usuario");
        textosEspanol.put("contrasenia", "Contraseña");
        textosEspanol.put("ingresar", "INGRESAR");
        textosEspanol.put("crear_cuenta", "Crear cuenta nueva");
        textosEspanol.put("bienvenido", "Bienvenido");
        textosEspanol.put("jugar", "Jugar / Seleccionar Nivel");
        textosEspanol.put("estadisticas", "Mis Estadísticas");
        textosEspanol.put("preferencias", "Preferencias");
        textosEspanol.put("cerrar_sesion", "Cerrar Sesión");
        textosEspanol.put("salir", "Salir del Juego");
        textosEspanol.put("volver", "Volver");
        textosEspanol.put("idioma", "Idioma:");
        textosEspanol.put("volumen", "Volumen:");
        textosEspanol.put("guardar", "Guardar Cambios");
        textosEspanol.put("victoria", "¡VICTORIA!");
        textosEspanol.put("completado", "¡Has completado todos los niveles!");
        textosEspanol.put("puntuacion", "Puntuación Total:");
        textosEspanol.put("nivel_max", "Nivel Máximo:");
        textosEspanol.put("partidas", "Partidas Jugadas:");
        textosEspanol.put("tiempo_total", "Tiempo Total:");
        textosEspanol.put("ranking", "Ranking Global (Top 3)");
        textosEspanol.put("seleccionar_nivel", "Selecciona un Nivel");
        textosEspanol.put("desafios", "Desafíos");
        textosEspanol.put("crear_desafio", "Crear Desafío");
        textosEspanol.put("recibidos", "Recibidos");
        textosEspanol.put("enviados", "Enviados");
        textosEspanol.put("aceptar", "Aceptar y Jugar");
        textosEspanol.put("rechazar", "Rechazar");
        textosEspanol.put("jugar_desafio", "¡Jugar ahora!");
        textosEspanol.put("ver_resultado", "Ver Resultado");
        textosEspanol.put("esperar_oponente", "Esperando al oponente...");
        textosEspanol.put("tu_turno", "¡Es tu turno!");
        textosEspanol.put("resultado_desafio", "Resultado del Desafío");
        textosEspanol.put("ganador", "¡GANADOR!");
        textosEspanol.put("empate", "¡EMPATE!");
        textosEspanol.put("ninguno", "(ninguno)");
        textosEspanol.put("sin_desafios", "No hay desafios todavia.");
        textosEspanol.put("oponente", "Oponente:");
        textosEspanol.put("nivel_reto", "Nivel del reto:");
        textosEspanol.put("campeonato", "Campeonato (todos)");
        textosEspanol.put("tiempo", "Tiempo:");
        textosEspanol.put("movimientos", "Movimientos:");
        textosEspanol.put("notif_desafio", " desafío(s) pendiente(s)");
        textosEspanol.put("ya_jugaste", "Ya jugaste. Esperando a");
        textosEspanol.put("ambos_jugaron", "¡Ambos jugaron! Resultado final:");
        textosEspanol.put("pendiente_aceptar", "Pendiente de aceptar");
        textosEspanol.put("tu_score", "Tu puntuacion:");
        textosEspanol.put("score_oponente", "Puntuación del oponente:");
        textosEspanol.put("avatar", "Cambiar foto de perfil:");

        textosIngles.put("victoria", "VICTORY!");
        textosIngles.put("completado", "You have completed all levels!");
        textosIngles.put("puntuacion", "Total Score:");
        textosIngles.put("nivel_max", "Max Level:");
        textosIngles.put("partidas", "Games Played:");
        textosIngles.put("tiempo_total", "Total Time:");
        textosIngles.put("ranking", "Global Ranking (Top 3)");
        textosIngles.put("seleccionar_nivel", "Select a Level");
        textosIngles.put("usuario", "Username");
        textosIngles.put("contrasenia", "Password");
        textosIngles.put("ingresar", "LOGIN");
        textosIngles.put("crear_cuenta", "Create new account");
        textosIngles.put("bienvenido", "Welcome");
        textosIngles.put("jugar", "Play / Select Level");
        textosIngles.put("estadisticas", "My Statistics");
        textosIngles.put("preferencias", "Preferences");
        textosIngles.put("cerrar_sesion", "Logout");
        textosIngles.put("salir", "Exit Game");
        textosIngles.put("volver", "Back");
        textosIngles.put("idioma", "Language:");
        textosIngles.put("volumen", "Volume:");
        textosIngles.put("guardar", "Save Changes");
        textosIngles.put("desafios", "Challenges");
        textosIngles.put("crear_desafio", "Create Challenge");
        textosIngles.put("recibidos", "Received");
        textosIngles.put("enviados", "Sent");
        textosIngles.put("aceptar", "Accept & Play");
        textosIngles.put("rechazar", "Reject");
        textosIngles.put("jugar_desafio", "Play now!");
        textosIngles.put("ver_resultado", "View Result");
        textosIngles.put("esperar_oponente", "Waiting for opponent...");
        textosIngles.put("tu_turno", "It's your turn!");
        textosIngles.put("resultado_desafio", "Challenge Result");
        textosIngles.put("ganador", "WINNER!");
        textosIngles.put("empate", "DRAW!");
        textosIngles.put("ninguno", "(none)");
        textosIngles.put("sin_desafios", "No challenges yet.");
        textosIngles.put("oponente", "Opponent:");
        textosIngles.put("nivel_reto", "Challenge level:");
        textosIngles.put("campeonato", "Championship (all)");
        textosIngles.put("tiempo", "Time:");
        textosIngles.put("movimientos", "Moves:");
        textosIngles.put("notif_desafio", " pending challenge(s)");
        textosIngles.put("ya_jugaste", "You played. Waiting for");
        textosIngles.put("ambos_jugaron", "Both played! Final result:");
        textosIngles.put("pendiente_aceptar", "Awaiting acceptance");
        textosIngles.put("tu_score", "Your score:");
        textosIngles.put("score_oponente", "Opponent score:");
        textosIngles.put("avatar", "Change profile picture:");
    }

    public String obtenerTexto(String clave, String idiomaUsuario) {
        if (idiomaUsuario != null && idiomaUsuario.equalsIgnoreCase("en")) {
            return textosIngles.getOrDefault(clave, clave);
        }
        return textosEspanol.getOrDefault(clave, clave);
    }

    public void cargarRecursos() {
        generarFuentesYSkin();
        
        try {
            musicaMenu = Gdx.audio.newMusic(Gdx.files.internal("assets/audios/menu_bgm.mp3"));
            musicaMenu.setLooping(true);
            musicaJuego = Gdx.audio.newMusic(Gdx.files.internal("assets/audios/juego_bgm.mp3"));
            musicaJuego.setLooping(true);
            System.out.println("Audio cargado correctamente.");
        } catch (Exception e) {
            System.err.println("ERROR AUDIO (internal): " + e.getMessage());
            try {
                java.io.File dir = new java.io.File("assets/audios");
                System.out.println("Carpeta audios existe: " + dir.exists() + " | Ruta: " + dir.getAbsolutePath());
                musicaMenu = Gdx.audio.newMusic(Gdx.files.absolute(dir.getAbsolutePath() + "/menu_bgm.mp3"));
                musicaMenu.setLooping(true);
                musicaJuego = Gdx.audio.newMusic(Gdx.files.absolute(dir.getAbsolutePath() + "/juego_bgm.mp3"));
                musicaJuego.setLooping(true);
                System.out.println("Audio cargado con ruta absoluta.");
            } catch (Exception e2) {
                System.err.println("ERROR AUDIO (absolute): " + e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    public void reproducirMusicaMenu(int volumenUsuario) {
        if (musicaJuego != null && musicaJuego.isPlaying()) {
            musicaJuego.stop();
        }
        if (musicaMenu != null && !musicaMenu.isPlaying()) {
            musicaMenu.setVolume(volumenUsuario / 100f);
            musicaMenu.play();
        } else if (musicaMenu != null) {
            musicaMenu.setVolume(volumenUsuario / 100f);
        }
    }

    public void reproducirMusicaJuego(int volumenUsuario) {
        if (musicaMenu != null && musicaMenu.isPlaying()) {
            musicaMenu.stop();
        }
        if (musicaJuego != null && !musicaJuego.isPlaying()) {
            musicaJuego.setVolume(volumenUsuario / 100f);
            musicaJuego.play();
        } else if (musicaJuego != null) {
            musicaJuego.setVolume(volumenUsuario / 100f);
        }
    }

    public void detenerMusica() {
        if (musicaMenu != null) musicaMenu.stop();
        if (musicaJuego != null) musicaJuego.stop();
    }

    private void generarFuentesYSkin() {
        fuentePrincipal = new BitmapFont();
        fuentePrincipal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fuentePrincipal.getData().setScale(1.4f);
        fuentePrincipal.setColor(Color.WHITE);

        fuenteTitulo = new BitmapFont();
        fuenteTitulo.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fuenteTitulo.getData().setScale(3.2f);

        BitmapFont fuenteSubtitulo = new BitmapFont();
        fuenteSubtitulo.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fuenteSubtitulo.getData().setScale(1.7f);

        BitmapFont fuenteBoton = new BitmapFont();
        fuenteBoton.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fuenteBoton.getData().setScale(1.5f);

        skinUI = new Skin();
        skinUI.add("default-font",  fuentePrincipal);
        skinUI.add("title-font",    fuenteTitulo);
        skinUI.add("subtitle-font", fuenteSubtitulo);
        skinUI.add("button-font",   fuenteBoton);

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        Texture texBlanco = new Texture(pm);
        skinUI.add("white", texBlanco);
        pm.dispose();

        Color colPanelFondo     = new Color(0.09f, 0.102f, 0.129f, 0.95f); // #171a21
        Color colBorde          = new Color(0.165f, 0.278f, 0.369f, 1f);   // #2a475e (Darker blue for panels)
        Color colBotonUp        = new Color(0.40f, 0.75f, 0.95f, 1f);      // #66c0f4 (Steam Accent Blue)
        Color colBotonOver      = new Color(0.46f, 0.85f, 1.0f, 1f);       // Brighter blue
        Color colBotonDown      = new Color(0.165f, 0.278f, 0.369f, 1f);   // Darker blue
        Color colSecUp          = new Color(0.24f, 0.267f, 0.314f, 1f);    // #3d4450 (Steam Grey Button)
        Color colSecOver        = new Color(0.349f, 0.376f, 0.427f, 1f);   // Lighter grey
        Color colSecDown        = new Color(0.165f, 0.18f, 0.22f, 1f);     // Darker grey
        Color colCampoFondo     = new Color(0.106f, 0.157f, 0.22f, 1f);    // #1b2838
        Color colCampoSeleccion = new Color(0.40f, 0.75f, 0.95f, 0.4f);    // Blue alpha
        Color colGris           = new Color(0.3f, 0.3f, 0.3f, 1f);

        skinUI.add("panel-bg",  new TextureRegionDrawable(texBlanco).tint(colPanelFondo),  Drawable.class);
        skinUI.add("border-bg", new TextureRegionDrawable(texBlanco).tint(colBorde),         Drawable.class);
        skinUI.add("border-panel-bg", new TextureRegionDrawable(texBlanco).tint(colBorde),   Drawable.class);
        skinUI.add("item-bg",   new TextureRegionDrawable(texBlanco).tint(colCampoFondo),   Drawable.class);

        TextButtonStyle estiloBoton = new TextButtonStyle();
        estiloBoton.up       = new TextureRegionDrawable(texBlanco).tint(colBotonUp);
        estiloBoton.over     = new TextureRegionDrawable(texBlanco).tint(colBotonOver);
        estiloBoton.down     = new TextureRegionDrawable(texBlanco).tint(colBotonDown);
        estiloBoton.disabled = new TextureRegionDrawable(texBlanco).tint(colGris);
        estiloBoton.font      = fuenteBoton;
        estiloBoton.fontColor = new Color(0.08f, 0.05f, 0.00f, 1f);
        skinUI.add("default", estiloBoton);

        TextButtonStyle estiloBotonSec = new TextButtonStyle();
        estiloBotonSec.up       = new TextureRegionDrawable(texBlanco).tint(colSecUp);
        estiloBotonSec.over     = new TextureRegionDrawable(texBlanco).tint(colSecOver);
        estiloBotonSec.down     = new TextureRegionDrawable(texBlanco).tint(colSecDown);
        estiloBotonSec.disabled = new TextureRegionDrawable(texBlanco).tint(colGris);
        estiloBotonSec.font      = fuenteBoton;
        estiloBotonSec.fontColor = new Color(0.75f, 0.80f, 1f, 1f);
        skinUI.add("secondary", estiloBotonSec);

        LabelStyle estiloTitulo = new LabelStyle();
        estiloTitulo.font      = fuenteTitulo;
        estiloTitulo.fontColor = new Color(1f, 1f, 1f, 1f);
        skinUI.add("title", estiloTitulo);

        LabelStyle estiloSubtitulo = new LabelStyle();
        estiloSubtitulo.font      = fuenteSubtitulo;
        estiloSubtitulo.fontColor = new Color(0.40f, 0.75f, 0.95f, 1f);
        skinUI.add("subtitle", estiloSubtitulo);

        LabelStyle estiloDefault = new LabelStyle();
        estiloDefault.font      = fuentePrincipal;
        estiloDefault.fontColor = Color.WHITE;
        skinUI.add("default", estiloDefault);

        LabelStyle estiloMensaje = new LabelStyle();
        estiloMensaje.font      = fuentePrincipal;
        estiloMensaje.fontColor = new Color(1f, 0.55f, 0.15f, 1f);
        skinUI.add("message", estiloMensaje);

        TextFieldStyle estiloTextField = new TextFieldStyle();
        estiloTextField.font             = fuentePrincipal;
        estiloTextField.fontColor        = Color.WHITE;
        estiloTextField.messageFontColor = new Color(0.5f, 0.55f, 0.65f, 1f);
        estiloTextField.background       = new TextureRegionDrawable(texBlanco).tint(colCampoFondo);
        estiloTextField.focusedBackground = new TextureRegionDrawable(texBlanco).tint(new Color(0.10f, 0.12f, 0.30f, 1f));
        estiloTextField.cursor           = new TextureRegionDrawable(texBlanco).tint(colBorde);
        estiloTextField.selection        = new TextureRegionDrawable(texBlanco).tint(colCampoSeleccion);
        skinUI.add("default", estiloTextField);

        com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle estiloScrollPane = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle();
        estiloScrollPane.background = new TextureRegionDrawable(texBlanco).tint(new Color(0f, 0f, 0f, 0f));
        estiloScrollPane.vScroll = new TextureRegionDrawable(texBlanco).tint(new Color(0.2f, 0.2f, 0.2f, 0.5f));
        estiloScrollPane.vScrollKnob = new TextureRegionDrawable(texBlanco).tint(colBorde);
        skinUI.add("default", estiloScrollPane);
    }

    public Skin getSkinUI() {
        return skinUI;
    }

    public void liberarRecursos() {
        if (fuentePrincipal != null) {
            fuentePrincipal.dispose();
        }
        if (fuenteTitulo != null) {
            fuenteTitulo.dispose();
        }
        if (skinUI != null) {
            skinUI.dispose();
        }
        if (musicaMenu != null) {
            musicaMenu.dispose();
        }
        if (musicaJuego != null) {
            musicaJuego.dispose();
        }
    }
}
