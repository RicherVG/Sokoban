package sokobanrg.model;

import java.io.Serializable;
import java.util.Date;

public class Partida implements Serializable {
    private static final long serialVersionUID = 2L;
    private int numeroNivel;
    private boolean completada;
    private int cantidadMovimientos;
    private int tiempoSegundos;
    private Date fechaPartida;

    public Partida(int numeroNivel, boolean completada, int cantidadMovimientos, int tiempoSegundos) {
        this.numeroNivel = numeroNivel;
        this.completada = completada;
        this.cantidadMovimientos = cantidadMovimientos;
        this.tiempoSegundos = tiempoSegundos;
        this.fechaPartida = new Date();
    }

    public int getNumeroNivel() {
        return numeroNivel;
    }

    public void setNumeroNivel(int numeroNivel) {
        this.numeroNivel = numeroNivel;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public int getCantidadMovimientos() {
        return cantidadMovimientos;
    }

    public void setCantidadMovimientos(int cantidadMovimientos) {
        this.cantidadMovimientos = cantidadMovimientos;
    }

    public int getTiempoSegundos() {
        return tiempoSegundos;
    }

    public void setTiempoSegundos(int tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
    }

    public Date getFechaPartida() {
        return fechaPartida;
    }

    public void setFechaPartida(Date fechaPartida) {
        this.fechaPartida = fechaPartida;
    }
}
