package sokobanrg;

import com.badlogic.gdx.Game;
import sokobanrg.model.Usuario;
import sokobanrg.servicio.AlmacenamientoBinario;
import sokobanrg.servicio.ManejadorRecursos;
import sokobanrg.pantalla.PantallaLogin;
import sokobanrg.pantalla.PantallaRegistro;

public class SokobanJuego extends Game {
    private AlmacenamientoBinario almacenamiento;
    private ManejadorRecursos manejadorRecursos;
    private Usuario usuarioActual;

    @Override
    public void create() {
        almacenamiento = new AlmacenamientoBinario();
        manejadorRecursos = new ManejadorRecursos();
        manejadorRecursos.cargarRecursos();
        mostrarPantallaLogin();
    }

    public AlmacenamientoBinario getAlmacenamiento() {
        return almacenamiento;
    }

    public ManejadorRecursos getManejadorRecursos() {
        return manejadorRecursos;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void mostrarPantallaLogin() {
        setScreen(new PantallaLogin(this));
    }

    public void mostrarPantallaRegistro() {
        setScreen(new PantallaRegistro(this));
    }

    public void mostrarPantallaMenu() {
        setScreen(new sokobanrg.pantalla.PantallaMenu(this));
    }

    public void mostrarPantallaSeleccionNivel() {
        setScreen(new sokobanrg.pantalla.PantallaSeleccionNivel(this));
    }

    public void mostrarPantallaJuego(int nivel) {
        setScreen(new sokobanrg.pantalla.PantallaJuego(this, nivel));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (manejadorRecursos != null) {
            manejadorRecursos.liberarRecursos();
        }
    }
}
