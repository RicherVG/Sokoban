package com.sokoban.pantalla;

/**
 * @author riche
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sokoban.SokobanJuego;
import com.sokoban.model.Usuario;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PantallaVictoria extends PantallaBase {

    public PantallaVictoria(SokobanJuego juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();

        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();
        if (skin == null) return;

        Usuario usuario = juegoSokoban.getUsuarioActual();
        String idioma = (usuario != null) ? usuario.getIdioma() : "es";

        int volumen = (usuario != null) ? usuario.getVolumen() : 100;
        juegoSokoban.getManejadorRecursos().reproducirMusicaMenu(volumen);

        Table tablaRaiz = new Table();
        tablaRaiz.setFillParent(true);
        escenario.addActor(tablaRaiz);

        Table tablaPanel = new Table();
        tablaPanel.setBackground(skin.getDrawable("panel-bg"));
        tablaPanel.pad(30, 40, 30, 40);

        Label titulo = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("victoria", idioma), skin, "title");
        Label subtitulo = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("completado", idioma), skin, "subtitle");
        tablaPanel.add(titulo).spaceBottom(6).row();
        tablaPanel.add(subtitulo).spaceBottom(20).row();

        if (usuario != null) {
            Table tablaDesglose = new Table();
            tablaDesglose.setBackground(skin.getDrawable("border-bg"));
            tablaDesglose.pad(12, 20, 12, 20);

            Map<Integer, Integer> mejores = usuario.getMejorPuntuacionPorNivel();
            int totalPts = 0;
            for (int n = 1; n <= 5; n++) {
                int pts = mejores.getOrDefault(n, 0);
                totalPts += pts;
                String medalla = pts > 3000 ? "Oro" : pts > 1500 ? "Plata" : "Bronce";
                Label lblNivel = new Label("Nivel " + n + ": " + pts + " pts  [" + medalla + "]", skin, "default");
                tablaDesglose.add(lblNivel).left().row();
            }
            Label lblTotal = new Label("Total: " + totalPts + " pts", skin, "subtitle");
            tablaDesglose.add(lblTotal).padTop(10).left().row();
            tablaPanel.add(tablaDesglose).spaceBottom(20).row();
        }

        Label lblRankingTitulo = new Label(juegoSokoban.getManejadorRecursos().obtenerTexto("ranking", idioma), skin, "subtitle");
        tablaPanel.add(lblRankingTitulo).spaceBottom(10).row();

        Table tablaRanking = new Table();
        List<Usuario> todos = juegoSokoban.getAlmacenamiento().cargarUsuarios();
        Collections.sort(todos, new Comparator<Usuario>() {
            @Override
            public int compare(Usuario u1, Usuario u2) {
                return Integer.compare(u2.getPuntuacionGeneral(), u1.getPuntuacionGeneral());
            }
        });

        String[] medallas = {"Oro  ", "Plata", "Bronce"};
        int max = Math.min(3, todos.size());
        for (int i = 0; i < max; i++) {
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

            String linea = medallas[i] + "  " + (i + 1) + ". " + u.getNombreUsuario() + " — " + u.getPuntuacionGeneral() + " pts";
            Label lblFila = new Label(linea, skin, "default");

            if (i == 0) lblFila.setColor(new Color(1f, 0.85f, 0.1f, 1f));
            else if (i == 1) lblFila.setColor(new Color(0.75f, 0.75f, 0.8f, 1f));
            else lblFila.setColor(new Color(0.8f, 0.5f, 0.3f, 1f));

            if (imgAvatar != null) {
                tablaRanking.add(imgAvatar).width(48).height(48).padRight(10).padBottom(8);
            } else {
                tablaRanking.add().width(48).height(48).padRight(10).padBottom(8);
            }
            tablaRanking.add(lblFila).left().padBottom(8).row();
        }
        tablaPanel.add(tablaRanking).spaceBottom(24).row();

        TextButton btnVolver = crearBoton(juegoSokoban.getManejadorRecursos().obtenerTexto("volver", idioma));
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaMenu();
            }
        });
        tablaPanel.add(btnVolver).width(250).height(48).row();

        tablaRaiz.add(tablaPanel).pad(10);
    }
}
