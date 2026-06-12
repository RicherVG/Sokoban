/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sokobanrg.model;

/**
 *
 * @author Gabriel
 */
public abstract class Juego {
    protected boolean activo;
    protected int nivelActual;
    protected int movimientosRealizados;

    public Juego() {
        this.activo = false;
        this.nivelActual = 1;
        this.movimientosRealizados = 0;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public void setNivelActual(int nivelActual) {
        this.nivelActual = nivelActual;
    }

    public int getMovimientosRealizados() {
        return movimientosRealizados;
    }

    public void setMovimientosRealizados(int movimientosRealizados) {
        this.movimientosRealizados = movimientosRealizados;
    }

    public abstract boolean cargarNivel(int numeroNivel);

    public abstract void reiniciarNivel();

    public abstract boolean verificarVictoria();
}
