package com.sokoban;
/**
 *
 * @author riche
 */

import com.badlogic.gdx.Game;
import com.sokoban.model.Desafio;
import com.sokoban.model.EstadoDesafio;
import com.sokoban.model.Usuario;
import com.sokoban.service.AlmacenamientoBinario;
import com.sokoban.service.ManejadorRecursos;
import com.sokoban.pantalla.PantallaLogin;
import com.sokoban.pantalla.PantallaRegistro;
import com.sokoban.pantalla.PantallaMenu;
import com.sokoban.pantalla.PantallaSeleccionNivel;
import com.sokoban.pantalla.PantallaJuego;
import com.sokoban.pantalla.PantallaPreferencias;
import com.sokoban.pantalla.PantallaEstadisticas;
import com.sokoban.pantalla.PantallaVictoria;
import com.sokoban.pantalla.PantallaDesafios;
import com.sokoban.pantalla.PantallaCrearDesafio;
import com.sokoban.pantalla.PantallaResultadoDesafio;
import java.util.Date;

public class SokobanJuego extends Game {
    private AlmacenamientoBinario almacenamiento;
    private ManejadorRecursos manejadorRecursos;
    private Usuario usuarioActual;
    private Desafio desafioActivo;
    private int acumPuntosDesafio;
    private int acumTiempoDesafio;
    private int acumMovimientosDesafio;

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

    public String getIdiomaActual() {
        return (usuarioActual != null) ? usuarioActual.getIdioma() : "es";
    }

    public void mostrarPantallaLogin() {
        setScreen(new PantallaLogin(this));
    }

    public void mostrarPantallaRegistro() {
        setScreen(new PantallaRegistro(this));
    }

    public void mostrarPantallaMenu() {
        setScreen(new PantallaMenu(this));
    }

    public void mostrarPantallaSeleccionNivel() {
        setScreen(new PantallaSeleccionNivel(this));
    }

    public void mostrarPantallaJuego(int nivel) {
        setScreen(new PantallaJuego(this, nivel));
    }

    public void mostrarPantallaPreferencias() {
        setScreen(new PantallaPreferencias(this));
    }

    public void mostrarPantallaEstadisticas() {
        setScreen(new PantallaEstadisticas(this));
    }

    public void mostrarPantallaVictoria() {
        setScreen(new PantallaVictoria(this));
    }

    public void mostrarPantallaDesafios() {
        setScreen(new PantallaDesafios(this));
    }

    public void mostrarPantallaCrearDesafio() {
        setScreen(new PantallaCrearDesafio(this));
    }

    public void mostrarPantallaResultadoDesafio(String desafioId) {
        setScreen(new PantallaResultadoDesafio(this, desafioId));
    }

    public Desafio getDesafioActivo() {
        return desafioActivo;
    }

    public void iniciarModoDesafio(Desafio desafio) {
        this.desafioActivo = desafio;
        this.acumPuntosDesafio = 0;
        this.acumTiempoDesafio = 0;
        this.acumMovimientosDesafio = 0;
    }

    public void acumularEstadisticasDesafio(int puntos, int tiempo, int movimientos) {
        acumPuntosDesafio += puntos;
        acumTiempoDesafio += tiempo;
        acumMovimientosDesafio += movimientos;
    }

    public Desafio completarModoDesafio() {
        if (desafioActivo == null) return null;
        String miNombre = usuarioActual.getNombreUsuario();
        boolean soyDestinatario = miNombre.equals(desafioActivo.getNombreDestinatario());

        if (soyDestinatario) {
            desafioActivo.setPuntuacionDestinatario(acumPuntosDesafio);
            desafioActivo.setTiempoDestinatario(acumTiempoDesafio);
            desafioActivo.setMovimientosDestinatario(acumMovimientosDesafio);
            desafioActivo.setEstado(EstadoDesafio.B_JUGO);
        } else {
            desafioActivo.setPuntuacionRemitente(acumPuntosDesafio);
            desafioActivo.setTiempoRemitente(acumTiempoDesafio);
            desafioActivo.setMovimientosRemitente(acumMovimientosDesafio);
            if (desafioActivo.getEstado() == EstadoDesafio.B_JUGO) {
                desafioActivo.setEstado(EstadoDesafio.COMPLETADO);
                desafioActivo.setFechaResolucion(new Date());
            }
        }
        almacenamiento.guardarDesafio(desafioActivo);
        Desafio resultado = desafioActivo;
        desafioActivo = null;
        acumPuntosDesafio = 0;
        acumTiempoDesafio = 0;
        acumMovimientosDesafio = 0;
        return resultado;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (manejadorRecursos != null) {
            manejadorRecursos.liberarRecursos();
        }
    }
}
