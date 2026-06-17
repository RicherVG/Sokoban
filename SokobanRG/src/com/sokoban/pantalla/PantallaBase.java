package com.sokoban.pantalla;
/**
 *
 * @author riche
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.SokobanJuego;
import com.sokoban.service.ManejadorRecursos;

public abstract class PantallaBase implements Screen {
    protected final SokobanJuego juegoSokoban;
    protected Stage escenario;
    protected OrthographicCamera camaraUI;
    protected FitViewport vista;

    public PantallaBase(SokobanJuego juegoSokoban) {
        this.juegoSokoban = juegoSokoban;
        this.camaraUI = new OrthographicCamera();
        this.vista = new FitViewport(800, 600, camaraUI);
        this.escenario = new Stage(vista);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(escenario);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.04f, 0.04f, 0.10f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        escenario.act(delta);
        escenario.draw();
    }

    @Override
    public void resize(int ancho, int alto) {
        vista.update(ancho, alto, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (escenario != null) {
            escenario.dispose();
        }
    }

    protected TextButton crearBoton(String texto) {
        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();
        if (skin != null) {
            return new TextButton(texto, skin);
        }
        return null;
    }

    protected Label crearEtiqueta(String texto, boolean esTitulo) {
        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();
        if (skin != null) {
            String estilo = esTitulo ? "title" : "default";
            return new Label(texto, skin, estilo);
        }
        return null;
    }

    protected TextField crearCampoTexto(String placeholder) {
        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();
        if (skin != null) {
            TextField campo = new TextField("", skin);
            campo.setMessageText(placeholder);
            return campo;
        }
        return null;
    }
}

