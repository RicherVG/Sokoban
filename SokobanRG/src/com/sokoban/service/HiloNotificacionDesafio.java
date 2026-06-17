package com.sokoban.service;

/**
 * @author riche
 */

import com.badlogic.gdx.Gdx;
import com.sokoban.model.Desafio;
import java.util.List;

public class HiloNotificacionDesafio extends Thread {
    private volatile boolean activo;
    private final String nombreUsuario;
    private final AlmacenamientoBinario almacenamiento;
    private final Runnable onCambio;
    private int ultimoConteo;

    public HiloNotificacionDesafio(String nombreUsuario, AlmacenamientoBinario almacenamiento, Runnable onCambio) {
        this.nombreUsuario = nombreUsuario;
        this.almacenamiento = almacenamiento;
        this.onCambio = onCambio;
        this.activo = true;
        this.ultimoConteo = -1;
        setDaemon(true);
        setName("HiloNotificacionDesafio-" + nombreUsuario);
    }

    public void detener() {
        this.activo = false;
        this.interrupt();
    }

    public int contarNotificaciones() {
        List<Desafio> pendientes = almacenamiento.cargarDesafiosPendientesParaDestinatario(nombreUsuario);
        List<Desafio> listos = almacenamiento.cargarDesafiosListosParaRemitente(nombreUsuario);
        return pendientes.size() + listos.size();
    }

    @Override
    public void run() {
        while (activo) {
            try {
                int conteoActual = contarNotificaciones();
                if (conteoActual != ultimoConteo) {
                    ultimoConteo = conteoActual;
                    if (onCambio != null && Gdx.app != null) {
                        Gdx.app.postRunnable(onCambio);
                    }
                }
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                activo = false;
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Error en HiloNotificacionDesafio: " + e.getMessage());
            }
        }
    }
}
