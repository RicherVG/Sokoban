package com.sokoban.pantalla;
/**
 *
 * @author riche
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.sokoban.model.Desafio;
import com.sokoban.model.ElementoMapa;
import com.sokoban.SokobanJuego;
import com.sokoban.model.Direccion;
import com.sokoban.model.JuegoSokoban;
import com.sokoban.model.Partida;
import com.sokoban.model.Usuario;
import com.sokoban.service.TemporizadorHilo;
import java.util.Date;

public class PantallaJuego extends PantallaBase {
    private int nivelActual;
    private JuegoSokoban logicaJuego;
    private TemporizadorHilo temporizador;
    private int vidas = 3;
    private int fallos = 0;
    private boolean sinVidas = false;
    private float oscurecimiento = 0f;
    private boolean perdiendo = false;
    private com.badlogic.gdx.graphics.glutils.ShapeRenderer shapeRendererHUD;
    
    private Label lblNombre;
    private Label lblNivel;
    private Label lblVidas;
    private Label lblFallos;
    private Label lblMovimientos;
    private Label lblTimer;
    private Label lblMejorPuntaje;
    private String lblTxtJugador = "Jugador: ";
    private String lblTxtNivel   = "Nivel: ";
    private String lblTxtVidas   = "Vidas: ";
    private String lblTxtFallos  = "Fallos: ";
    private String lblTxtMov     = "Mov: ";
    
    private Table tablaJuego; 

    public PantallaJuego(SokobanJuego juego, int nivel) {
        super(juego);
        this.nivelActual = nivel;
    }

    @Override
    public void show() {
        super.show();
        shapeRendererHUD = new com.badlogic.gdx.graphics.glutils.ShapeRenderer();

        Usuario u = juegoSokoban.getUsuarioActual();
        Desafio desafio = juegoSokoban.getDesafioActivo();
        if (desafio != null) {
            sinVidas = desafio.isSinVidas();
        } else if (u != null) {
            sinVidas = u.isModoSinVidas();
        } else {
            sinVidas = false;
        }
        int volumen = (u != null) ? u.getVolumen() : 100;
        juegoSokoban.getManejadorRecursos().reproducirMusicaJuego(volumen);

        logicaJuego = new JuegoSokoban();
        if (!logicaJuego.cargarNivel(nivelActual)) {
            juegoSokoban.mostrarPantallaSeleccionNivel();
            return;
        }

        if (juegoSokoban.getManejadorRecursos().getSkinUI() == null) {
            return;
        }

        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();

        Table tablaExterior = new Table();
        tablaExterior.setFillParent(true);
        escenario.addActor(tablaExterior);

        // Panel superior para el HUD
        Table hudPanel = new Table();
        hudPanel.setBackground(skin.getDrawable("panel-bg"));
        hudPanel.pad(12, 20, 12, 20);
        
        String idioma = juegoSokoban.getIdiomaActual();
        Usuario usuarioHud = juegoSokoban.getUsuarioActual();
        String nombre = (usuarioHud != null) ? usuarioHud.getNombreCompleto() : (idioma.equals("en") ? "Guest" : "Invitado");

        String txtJugador = idioma.equals("en") ? "Player: " : "Jugador: ";
        String txtNivel   = idioma.equals("en") ? "Level: "  : "Nivel: ";
        String txtVidas   = idioma.equals("en") ? "Lives: "  : "Vidas: ";
        String txtFallos  = idioma.equals("en") ? "Misses: " : "Fallos: ";
        String txtMov     = idioma.equals("en") ? "Moves: "  : "Mov: ";
        String txtTiempo  = idioma.equals("en") ? "Time: 0s" : "Tiempo: 0s";
        String txtMejor   = idioma.equals("en") ? "Best: " : "Record: ";

        lblNombre     = new Label(txtJugador + nombre, skin, "default");
        lblNivel      = new Label(txtNivel + nivelActual, skin, "default");
        lblVidas      = new Label(txtVidas + (sinVidas ? "INF" : vidas), skin, "default");
        lblFallos     = new Label(txtFallos + fallos, skin, "default");
        lblMovimientos = new Label(txtMov + "0", skin, "default");
        lblTimer      = new Label(txtTiempo, skin, "title");

        
        int mejorPts = 0;
        if (u != null && u.getMejorPuntuacionPorNivel() != null) {
            mejorPts = u.getMejorPuntuacionPorNivel().getOrDefault(nivelActual, 0);
        }
        lblMejorPuntaje = new Label(txtMejor + mejorPts, skin, "default");
        lblMejorPuntaje.setColor(1f, 0.85f, 0.1f, 1f); 
        
        lblTxtJugador  = txtJugador;
        lblTxtNivel    = txtNivel;
        lblTxtVidas    = txtVidas;
        lblTxtFallos   = txtFallos;
        lblTxtMov      = txtMov;
        final String lblTxtTiempo = idioma.equals("en") ? "Time: " : "Tiempo: ";

        hudPanel.add(lblNombre).expandX().align(Align.left);
        hudPanel.add(lblNivel).expandX().align(Align.center);
        hudPanel.add(lblVidas).expandX().align(Align.center);
        hudPanel.add(lblFallos).expandX().align(Align.center);
        hudPanel.add(lblMovimientos).expandX().align(Align.center);
        hudPanel.add(lblMejorPuntaje).expandX().align(Align.center);
        hudPanel.add(lblTimer).expandX().align(Align.right);

        
        tablaJuego = new Table();
        TableroActor tablero = new TableroActor();
        tablaJuego.add(tablero).expand().fill();

        
        Table controlesPanel = new Table();
        String txtReiniciar = idioma.equals("en") ? "Restart Level" : "Reiniciar Nivel";
        String txtRendirse  = idioma.equals("en") ? "Give Up"       : "Rendirse y Volver";
        TextButton btnReiniciar = crearBoton(txtReiniciar);
        TextButton btnVolver    = crearBoton(txtRendirse);
        
        btnReiniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logicaJuego.reiniciarNivel();
                fallos++;
                if (sinVidas) {
                    actualizarHUD();
                } else {
                    if (fallos >= vidas) {
                        
                        guardarPartida(false);
                        mostrarMensajeCentro("¡Te has quedado sin vidas!");
                        perdiendo = true;
                        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                            @Override
                            public void run() {
                                terminarNivelPorFallo();
                            }
                        }, 2f);
                    } else {
                        actualizarHUD();
                    }
                }
            }
        });

        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                guardarPartida(false);
                terminarNivelPorFallo();
            }
        });

        controlesPanel.add(btnReiniciar).width(200).height(48).padRight(20);
        controlesPanel.add(btnVolver).width(200).height(48);

        tablaExterior.add(hudPanel).expandX().fillX().top().row();
        tablaExterior.add(tablaJuego).expand().fill().row();
        tablaExterior.add(controlesPanel).expandX().fillX().bottom().padBottom(20);
        temporizador = new TemporizadorHilo(new Runnable() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (lblTimer != null) {
                            lblTimer.setText(lblTxtTiempo + temporizador.getSegundosTranscurridos() + "s");
                        }
                    }
                });
            }
        });
        temporizador.start();
    }
    
    @Override
    public void render(float delta) {
        super.render(delta);
        manejarEntrada();

        if (perdiendo && shapeRendererHUD != null) {
            oscurecimiento = Math.min(1.0f, oscurecimiento + delta * 0.5f);
            Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRendererHUD.setProjectionMatrix(camaraUI.combined);
            shapeRendererHUD.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
            shapeRendererHUD.setColor(new Color(0f, 0f, 0f, oscurecimiento));
            shapeRendererHUD.rect(0, 0, vista.getWorldWidth(), vista.getWorldHeight());
            shapeRendererHUD.end();
            
            Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
        }
    }

    private void manejarEntrada() {
        if (!logicaJuego.isActivo()) return;

        boolean movido = false;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            movido = logicaJuego.mover(Direccion.ARRIBA);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            movido = logicaJuego.mover(Direccion.ABAJO);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            movido = logicaJuego.mover(Direccion.IZQUIERDA);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            movido = logicaJuego.mover(Direccion.DERECHA);
        }

        if (movido) {
            actualizarHUD();
            if (logicaJuego.verificarVictoria()) {
                final int tiempoFinal = temporizador != null ? temporizador.getSegundosTranscurridos() : 0;
                final int movsFinal = logicaJuego.getMovimientosRealizados();
                guardarPartida(true);

                com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                    @Override
                    public void run() {
                        Desafio desafio = juegoSokoban.getDesafioActivo();
                        if (desafio != null) {
                            int puntos = Desafio.calcularPuntuacion(nivelActual, tiempoFinal, movsFinal, sinVidas);
                            juegoSokoban.acumularEstadisticasDesafio(puntos, tiempoFinal, movsFinal);

                            if (desafio.getNivel() == 0 && nivelActual < 5) {
                                juegoSokoban.mostrarPantallaJuego(nivelActual + 1);
                            } else {
                                Desafio completado = juegoSokoban.completarModoDesafio();
                                juegoSokoban.mostrarPantallaResultadoDesafio(completado.getId());
                            }
                        } else if (nivelActual >= 5) {
                            juegoSokoban.mostrarPantallaVictoria();
                        } else {
                            juegoSokoban.mostrarPantallaSeleccionNivel();
                        }
                    }
                }, 2f);
            }
        }
    }

    private void actualizarHUD() {
        lblVidas.setText(lblTxtVidas + (sinVidas ? "INF" : (vidas - fallos)));
        lblFallos.setText(lblTxtFallos + fallos);
        lblMovimientos.setText(lblTxtMov + logicaJuego.getMovimientosRealizados());
    }
    
    private void guardarPartida(boolean victoria) {
        if (temporizador != null) {
            temporizador.detener();
        }
        Usuario u = juegoSokoban.getUsuarioActual();
        if (u != null) {
            Partida p = new Partida(
                nivelActual, 
                victoria, 
                logicaJuego.getMovimientosRealizados(), 
                temporizador != null ? temporizador.getSegundosTranscurridos() : 0
            );

            u.agregarPartida(p, sinVidas);
            juegoSokoban.getAlmacenamiento().guardarUsuario(u);
        }
    }

    private void mostrarMensajeCentro(String texto) {
        Skin skin = juegoSokoban.getManejadorRecursos().getSkinUI();
        Label lblMensaje = new Label(texto, skin, "title");
        lblMensaje.setColor(com.badlogic.gdx.graphics.Color.RED);
        
        Table centerTable = new Table();
        centerTable.setFillParent(true);
        centerTable.add(lblMensaje).pad(20);
        escenario.addActor(centerTable);
    }

    private void terminarNivelPorFallo() {
        Desafio desafio = juegoSokoban.getDesafioActivo();
        if (desafio != null) {
            int tiempoF = temporizador != null ? temporizador.getSegundosTranscurridos() : 0;
            int movsF = logicaJuego.getMovimientosRealizados();
            juegoSokoban.acumularEstadisticasDesafio(0, tiempoF, movsF);
            Desafio completado = juegoSokoban.completarModoDesafio();
            juegoSokoban.mostrarPantallaResultadoDesafio(completado.getId());
        } else {
            juegoSokoban.mostrarPantallaSeleccionNivel();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (temporizador != null) {
            temporizador.detener();
        }
        if (shapeRendererHUD != null) {
            shapeRendererHUD.dispose();
        }
    }

    private class TableroActor extends Widget {
        private ShapeRenderer shapeRenderer;
        private Texture texPared, texCaja, texJugador, texCajaDestino;

        public TableroActor() {
            shapeRenderer = new ShapeRenderer();
            if (Gdx.files.internal("assets/textures/pared.png").exists()) texPared = new Texture(Gdx.files.internal("assets/textures/pared.png"));
            if (Gdx.files.internal("assets/textures/caja.png").exists()) texCaja = new Texture(Gdx.files.internal("assets/textures/caja.png"));
            if (Gdx.files.internal("assets/textures/jugador.png").exists()) texJugador = new Texture(Gdx.files.internal("assets/textures/jugador.png"));
            if (Gdx.files.internal("assets/textures/caja_destino.png").exists()) texCajaDestino = new Texture(Gdx.files.internal("assets/textures/caja_destino.png"));
        }

        @Override
        public float getPrefWidth() {
            return 400; 
        }

        @Override
        public float getPrefHeight() {
            return 400;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.end();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
            
            ElementoMapa[][] mapa = logicaJuego.getMapaActual();
            if (mapa == null) {
                 batch.begin();
                 return;
            }
            int filas = logicaJuego.getFilasTotales();
            int cols = logicaJuego.getColumnasTotales();
            
            Vector2 stagePos = localToStageCoordinates(new Vector2(0, 0));
            float absX = stagePos.x;
            float absY = stagePos.y;
            
            float cellSize = Math.min(getWidth() / cols, getHeight() / filas);
            float startX = absX + (getWidth() - (cols * cellSize)) / 2;
            float startY = absY + (getHeight() - (filas * cellSize)) / 2;
            
          
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0.12f, 0.15f, 0.25f, 1f)); // azul grisaceo
            for(int i=0; i<filas; i++){
                for(int j=0; j<cols; j++){
                     shapeRenderer.rect(startX + j*cellSize, startY + (filas - 1 - i)*cellSize, cellSize, cellSize);
                }
            }
            shapeRenderer.end();

            float tiempoMs = (float)(System.currentTimeMillis() % 2000L) / 2000f;
            float pulse = 0.6f + 0.4f * (float)Math.sin(tiempoMs * 2.0 * Math.PI);
            
            Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(4);
            shapeRenderer.setColor(new Color(1f, 0.2f, 0.2f, pulse));
            for(int i=0; i<filas; i++){
                for(int j=0; j<cols; j++){
                     ElementoMapa e = mapa[i][j];
                     if(e == ElementoMapa.DESTINO || e == ElementoMapa.JUGADOR_EN_DESTINO || e == ElementoMapa.CAJA_EN_DESTINO) {
                         float x = startX + j*cellSize;
                         float y = startY + (filas - 1 - i)*cellSize;
                         shapeRenderer.line(x + cellSize*0.25f, y + cellSize*0.25f, x + cellSize*0.75f, y + cellSize*0.75f);
                         shapeRenderer.line(x + cellSize*0.25f, y + cellSize*0.75f, x + cellSize*0.75f, y + cellSize*0.25f);
                     }
                }
            }
            shapeRenderer.end();
            Gdx.gl.glLineWidth(1);
            Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND);

            batch.begin();
            for(int i=0; i<filas; i++){
                for(int j=0; j<cols; j++){
                     ElementoMapa e = mapa[i][j];
                     float x = startX + j*cellSize;
                     float y = startY + (filas - 1 - i)*cellSize;
                     
                     Texture tex = null;
                     if(e == ElementoMapa.PARED) tex = texPared;
                     else if(e == ElementoMapa.CAJA) tex = texCaja;
                     else if(e == ElementoMapa.CAJA_EN_DESTINO) tex = texCajaDestino;
                     else if(e == ElementoMapa.JUGADOR || e == ElementoMapa.JUGADOR_EN_DESTINO) tex = texJugador; // Jugador en destino usa la misma imagen que el jugador
                     
                     if(tex != null) {
                         batch.draw(tex, x, y, cellSize, cellSize);
                     }
                }
            }
        }
    }
}
