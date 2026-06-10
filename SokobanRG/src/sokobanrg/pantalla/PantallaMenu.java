package sokobanrg.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import sokobanrg.SokobanJuego;
import sokobanrg.model.Usuario;

public class PantallaMenu extends PantallaBase {
    private Label lblBienvenida;

    public PantallaMenu(SokobanJuego juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();
        
        if (juegoSokoban.getManejadorRecursos().getSkinUI() == null) {
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

        lblBienvenida = crearEtiqueta("Bienvenido", true);
        actualizarDatosUsuario();

        TextButton btnJugar = crearBoton("Jugar / Seleccionar Nivel");
        TextButton btnEstadisticas = crearBoton("Mis Estadísticas");
        TextButton btnRanking = crearBoton("Ranking Global");
        TextButton btnCerrarSesion = crearBoton("Cerrar Sesión");
        TextButton btnSalir = crearBoton("Salir del Juego");

        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.mostrarPantallaSeleccionNivel();
            }
        });

        btnCerrarSesion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juegoSokoban.setUsuarioActual(null);
                juegoSokoban.mostrarPantallaLogin();
            }
        });

        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        tablaPanel.add(lblBienvenida).padBottom(40).row();
        tablaPanel.add(btnJugar).width(300).height(48).padBottom(15).row();
        tablaPanel.add(btnEstadisticas).width(300).height(48).padBottom(15).row();
        tablaPanel.add(btnRanking).width(300).height(48).padBottom(15).row();
        tablaPanel.add(btnCerrarSesion).width(300).height(48).padBottom(15).row();
        tablaPanel.add(btnSalir).width(300).height(48);

        tablaBorde.add(tablaPanel).pad(2);
        tablaExterior.add(tablaBorde);
    }

    public void actualizarDatosUsuario() {
        Usuario usuario = juegoSokoban.getUsuarioActual();
        if (usuario != null && lblBienvenida != null) {
            lblBienvenida.setText("Bienvenido, " + usuario.getNombreCompleto());
        }
    }
}
